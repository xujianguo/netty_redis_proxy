package com.gdut.xujianguo;

import com.gdut.xujianguo.server.ProxyServer;

/**
 * 系统启动类
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年7月30日
 */
public class Main {
	public static void main(String[] args) {
		ProxyServer server = new ProxyServer();
		try {
			server.startup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
