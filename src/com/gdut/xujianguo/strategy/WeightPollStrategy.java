package com.gdut.xujianguo.strategy;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.gdut.xujianguo.param.Instance;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 权重轮询策略
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月2日
 */
public class WeightPollStrategy extends Strategy {
	//有序实例列表，按照权重排序
	private TreeMap<Instance, Integer> weightInstances;

	public WeightPollStrategy(Map<Instance, Channel> instance2Channel) {
		super(instance2Channel);
		initWeightInstances();
	}

	/**
	 * 初始化weightInstances
	 */
	private void initWeightInstances() {
		weightInstances = new TreeMap<Instance, Integer>(new InstanceComparator());
		buildWeightMap();
	}
	
	/**
	 * 构建有序Map
	 */
	private void buildWeightMap() {
		for(Instance instance : instance2Channel.keySet()) {
			weightInstances.put(instance, instance.getWeight());
		}
	}
	
	/**
	 * 算法核心
	 * @return
	 */
	private Instance maxWeight() {
		//如果列表为空，需要重建列表
		if(weightInstances.size() == 0) {
			buildWeightMap();
		}
		//获取最大weight的那个instance
		Entry<Instance, Integer> first = weightInstances.firstEntry();
		//如果权重大于1就减1
		if(first.getValue() > 1) {
			weightInstances.replace(first.getKey(), first.getValue()-1);
		} else {
			weightInstances.remove(first.getKey());
		}
		//返回最大weight的instance
		return first.getKey();
	}

	/**
	 * 选择结果
	 */
	@Override
	public Channel select(ChannelHandlerContext ctx) {
		return instance2Channel.get(maxWeight());
	}
	
	/**
	 * 排序器
	 * @author xujianguo
	 * @email ray_xujianguo@yeah.net
	 * @time 2015年8月2日
	 */
	private class InstanceComparator implements Comparator<Instance> {
		@Override
		public int compare(Instance o1, Instance o2) {
			return o1.getWeight() > o2.getWeight() ? -1 : o1.getWeight() < o2.getWeight() ? 1 : 0;
		}
	}
}
