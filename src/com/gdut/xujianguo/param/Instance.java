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
	
	public Instance() {}
	
	public Instance(String address, int port) {
		this.address = address;
		this.port = port;
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
	
	@Override
	public String toString() {
		return "address => " + address + ", " + "port => " + port;
	}
}
