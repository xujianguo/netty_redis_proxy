package com.gdut.xujianguo.server;

import com.gdut.xujianguo.param.Param;

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
	//参数
	private Param param;
	
	public ProxyInitializer(Param param) {
		this.param = param;
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
			new ProxyFontHandler(param)
		);
	}
}
