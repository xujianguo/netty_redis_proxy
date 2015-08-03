package com.gdut.xujianguo.strategy;

import java.util.Map;
import java.util.Random;

import com.gdut.xujianguo.param.Instance;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 随机选择策略
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月3日
 */
public class RandomStrategy extends Strategy {
	//随机生成器
	private static Random builder = new Random();

	public RandomStrategy(Map<Instance, Channel> instance2Channel) {
		super(instance2Channel);
	}
	
	/**
	 * 随机返回一个Instance实例
	 * @return
	 */
	private Instance randomInstance() {
		int index = builder.nextInt(instance2Channel.size());
		return (Instance) instance2Channel.keySet().toArray()[index];
	}

	/**
	 * 选择结果
	 */
	@Override
	public Channel select(ChannelHandlerContext ctx) {
		return instance2Channel.get(randomInstance());
	}
}
