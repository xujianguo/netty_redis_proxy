package com.gdut.xujianguo.server;

import java.util.List;

import com.gdut.xujianguo.param.Instance;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 代理的前端处理
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月1日
 */
public class ProxyFontHandler extends ChannelHandlerAdapter {
	//Redis的实例列表
	private List<Instance> instances;
	
	public ProxyFontHandler(List<Instance> instances) {
		this.instances = instances;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}
}
