package com.gdut.xujianguo.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdut.xujianguo.param.Instance;
import com.gdut.xujianguo.param.Param;
import com.gdut.xujianguo.strategy.Selector;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;

/**
 * 代理的前端处理
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月1日
 */
public class ProxyFontHandler extends ChannelHandlerAdapter {
	//参数
	private Param param;
	//Redis的实例列表
	private List<Instance> instances;
	//实例跟Channel组成的Map
	private volatile Map<Instance, Channel> backChannels; 
	//Channel选择器
	private Selector selector;
	
	public ProxyFontHandler(Param param) {
		this.param = param;
		this.instances = this.param.getRedisInstances();
		backChannels = new HashMap<Instance, Channel>();
	}
	
	/**
	 * 通道可用的时候初始化各个Redis的连接
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final Channel fontChannel = ctx.channel();
		for(Instance instance : instances) {
			startInstance(fontChannel, instance);
		}
		//初始化selector
		selector = new Selector(param.getStrategy(), backChannels);
	}
	
	/**
	 * 连接到Redis实例中
	 * @param fontChannel
	 * @param instance
	 */
	private void startInstance(Channel fontChannel, Instance instance) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap
			.group(fontChannel.eventLoop())
			.channel(fontChannel.getClass())
			.handler(new ProxyBackHandler(fontChannel))
			.option(ChannelOption.AUTO_READ, false);
		ChannelFuture future = bootstrap.connect(instance.getAddress(), instance.getPort());
		backChannels.put(instance, future.channel());
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					fontChannel.read();
				} else {
					fontChannel.close();
				}
			}
		});
	}
	
	/**
	 * 读到数据的时候，将请求分配到Redis上
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Channel selectChannel = selector.selectChannel(ctx);
		if(selectChannel.isActive()) {
			selectChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()) {
						ctx.channel().read();
					} else {
						future.channel().close();
					}
				}
			});
		}
	}
	
	/**
	 * Channel不用的时候flush并关闭
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		for(Channel channel : backChannels.values()) {
			if(channel != null) {
				closeOnFlush(channel);
			}
		}
	}
	
	/**
	 * 异常出现时候关闭资源
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		closeOnFlush(ctx.channel());
	}
	
	/**
	 * 清空缓存，然后关闭Channel
	 * @param channel
	 */
	public static void closeOnFlush(Channel channel) {
		if(channel.isActive()) {
			channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
}
