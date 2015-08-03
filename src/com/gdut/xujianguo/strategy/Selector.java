package com.gdut.xujianguo.strategy;

import java.util.Map;

import com.gdut.xujianguo.param.Instance;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Channel选择器
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月2日
 */
public class Selector {
	//选择策略
	private final Strategy strategy;
	
	/**
	 * 静态工厂模式
	 * @param type 类型
	 * @param instance2Channel 映射
	 */
	public Selector(String type, Map<Instance, Channel> instance2Channel) {
		switch(type) {
		//权重轮询
		case "weight_poll":
			strategy = new WeightPollStrategy(instance2Channel);
			break;
		//随机选择
		case "random":
			strategy = new RandomStrategy(instance2Channel);
			break;
		//ip哈希
		case "ip_hash":
			strategy = new IPHashStrategy(instance2Channel);
			break;
		default:
			strategy = null;
		}
	}
	
	/**
	 * 选择Channel
	 * @return
	 */
	public Channel selectChannel(ChannelHandlerContext ctx) {
		return strategy.select(ctx);
	}
}
