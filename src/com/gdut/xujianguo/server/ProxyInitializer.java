package com.gdut.xujianguo.server;

import java.util.List;

import com.gdut.xujianguo.param.Instance;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 代理类的初始化
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月1日
 */
public class ProxyInitializer extends ChannelInitializer<SocketChannel> {
	//Redis是实例信息
	private List<Instance> instances;
	
	public ProxyInitializer(List<Instance> instances) {
		this.instances = instances;
	}

	/**
	 * 初始化Channel
	 * 		1.日志处理
	 * 		2.代理处理
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(
			new LoggingHandler(LogLevel.INFO),
			new ProxyFontHandler(instances)
		);
	}
}
