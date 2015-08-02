package com.gdut.xujianguo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.gdut.xujianguo.param.Param;

public final class ProxyServer {
	// 配置参数
	private Param param;

	public ProxyServer() {
		// 加载参数
		param = new Param();
	}

	public void startup() throws Exception {
		// 创建事件执行组
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// 通信通道的启动类
			ServerBootstrap bootstrap = new ServerBootstrap();
			// 执行事件接收和处理类
			bootstrap
				.group(bossGroup, workerGroup)
				// 设置通讯Channel的创建类
				.channel(NioServerSocketChannel.class)
				// 处理服务器请求
				.handler(new LoggingHandler(LogLevel.INFO))
				// 处理对应的请求
				.childHandler(new ProxyInitializer(param.getRedisInstances()))
				// 设置可选项
				.childOption(ChannelOption.AUTO_READ, false)
				// 创建Channel并且绑定端口
				.bind(param.getServerPort())
				// 同步操作
				.sync().channel().closeFuture().sync();
		} finally {
			// 合理关闭资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
