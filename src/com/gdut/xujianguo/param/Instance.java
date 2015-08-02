package com.gdut.xujianguo.param;

/**
 * Redis实例
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年7月30日
 */
public class Instance {
	//IP地址
	private String address;
	//端口
	private int port;
	//权重
	private int weight = 1;
	
	public Instance() {}
	
	public Instance(String address, int port, int weight) {
		this.address = address;
		this.port = port;
		this.weight = weight;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Instance) {
			Instance target = (Instance)obj;
			if(target.getAddress().equals(address) && target.getPort() == port) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return address.hashCode() + port;
	}
	
	@Override
	public String toString() {
		return "address => " + address + ", " + "port => " + port + ", " + "weight => " + weight;
	}
}
