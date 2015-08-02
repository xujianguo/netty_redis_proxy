package com.gdut.xujianguo;

import org.apache.log4j.Logger;

import com.gdut.xujianguo.server.ProxyServer;

/**
 * 系统启动类
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年7月30日
 */
public class Main {
	private static Logger log = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		ProxyServer server = new ProxyServer();
		try {
			server.startup();
		} catch (Exception e) {
			log.error("[Main->startup启动失败]", e);
		}
	}
}
