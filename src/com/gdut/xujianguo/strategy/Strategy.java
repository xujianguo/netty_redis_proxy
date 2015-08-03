package com.gdut.xujianguo.strategy;

import java.util.Map;

import com.gdut.xujianguo.param.Instance;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 抽象选择策略（策略模式）
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月2日
 */
public abstract class Strategy {
	//映射
	protected Map<Instance, Channel> instance2Channel;
	
	public Strategy(Map<Instance, Channel> instance2Channel) {
		this.instance2Channel = instance2Channel;
	}
	
	/**
	 * 选择结果
	 * @return
	 */
	public abstract Channel select(ChannelHandlerContext ctx);
}
