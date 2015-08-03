package com.gdut.xujianguo.strategy;

import java.net.InetSocketAddress;
import java.util.Map;

import com.gdut.xujianguo.param.Instance;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * IP哈希策略
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月3日
 */
public class IPHashStrategy extends Strategy {

	public IPHashStrategy(Map<Instance, Channel> instance2Channel) {
		super(instance2Channel);
	}
	
	/**
	 * 从ctx中获取IP
	 * @param ctx
	 * @return
	 */
	private String getIP(ChannelHandlerContext ctx) {
		InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
		return address.getAddress().getHostAddress();
	}
	
	/**
	 * 根据ip的前三位计算hash值
	 * @param ip
	 * @return
	 */
	private int findIndex(String ip) {
		String[] strs = ip.split("\\.");
		int hash = 89;
		for(int i = 0; i < 3; i++) {
			hash = (hash * 113 + Integer.valueOf(strs[i])) % 6271;
		}
		return hash % instance2Channel.size();
	}

	/**
	 * 返回结果
	 */
	@Override
	public Channel select(ChannelHandlerContext ctx) {
		return instance2Channel.get(instance2Channel.keySet().toArray()[findIndex(getIP(ctx))]);
	}
}
