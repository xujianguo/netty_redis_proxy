package com.gdut.xujianguo.strategy;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.gdut.xujianguo.param.Instance;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 一致性哈希策略
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月4日
 */
public class ConsistenHashStrategy extends Strategy {
	//节点
	private TreeMap<Long, Instance> nodes;
	//每个节点承载的虚拟节点数
	private final static int NODE_NUMBER = 100;

	public ConsistenHashStrategy(Map<Instance, Channel> instance2Channel) {
		super(instance2Channel);
		init();
	}
	
	/**
	 * 初始化哈希环
	 */
	private void init() {
		nodes = new TreeMap<Long, Instance>();
		int number = 0;
		for(Instance instance : instance2Channel.keySet()) {
			for(int i = 0; i < NODE_NUMBER; i++) {
				nodes.put(hash("instance-"+(number++)+"-node-"+i), instance);
			}
		}
	}
	
	/**
	 * 查找节点
	 * @param key
	 * @return
	 */
	public Instance findNode(String key) {
		SortedMap<Long, Instance> tail = nodes.tailMap(hash(key));
		if(tail.size() == 0) {
			return nodes.get(nodes.firstKey());
		}
		return tail.get(tail.firstKey());
	}
	
	/**
	 * MurmurHash算法
	 * @param key
	 * @return
	 */
	private Long hash(String key) {
		ByteBuffer buffer = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;
		ByteOrder order = buffer.order();
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		long m = 0xc6a4a7935bd1e995L;
		int r = 47;
		long h = seed ^ (buffer.remaining() * m);
		long k;
		while (buffer.remaining() >= 8) {
			k = buffer.getLong();
			k *= m;
			k ^= k >>> r;
			k *= m;
			h ^= k;
			h *= m;
		}
		if (buffer.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(
					ByteOrder.LITTLE_ENDIAN);
			finish.put(buffer).rewind();
			h ^= finish.getLong();
			h *= m;
		}
		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;
		buffer.order(order);
		return h;
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

	@Override
	public Channel select(ChannelHandlerContext ctx) {
		return instance2Channel.get(findNode(getIP(ctx)));
	}
}
