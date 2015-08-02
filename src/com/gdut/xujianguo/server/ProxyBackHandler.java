package com.gdut.xujianguo.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 代理的后端处理
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月1日
 */
public class ProxyBackHandler extends ChannelHandlerAdapter {
	//代理的前端Channel
	private final Channel fontChannel;
	
	public ProxyBackHandler(Channel fontChannel) {
		this.fontChannel = fontChannel;
	}
	
	/**
	 * Channel激活时候读取信息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.read();
		ctx.write(Unpooled.EMPTY_BUFFER);
	}
	
	/**
	 * Channel读到东西的时候就用fontChannel写出
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		fontChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
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
	
	/**
	 * Channel不可用的时候关闭资源
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ProxyFontHandler.closeOnFlush(fontChannel);
	}
	
	/**
	 * 异常出现时候关闭资源
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ProxyFontHandler.closeOnFlush(ctx.channel());
	}
}
