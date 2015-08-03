package com.gdut.xujianguo.param;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * 参数加载类
 * @author xujianguo
 * @email ray_xujianguo@yeah.net
 * @time 2015年8月1日
 */
public class Param {
	private static Logger log = Logger.getLogger(Param.class);
	//核心配置文件的路径
	private static final String CORE_FILE_PATH = "resources/core.json"; 
	//服务器端口
	private static int serverPort;
	//策略
	private static String strategy;
	//Redis实例列表
	private static List<Instance> instances; 
	
	/**
	 * 构造方法中初始化配置
	 */
	public Param() {
		try {
			String coreJson = readJsonFromFile(CORE_FILE_PATH);
			serverPort = (int) JSON.parseObject(coreJson).getJSONObject("server").get("port");
			strategy = (String) JSON.parseObject(coreJson).get("strategy");
			instances = JSON.parseArray(JSON.parseObject(coreJson).getJSONArray("instances").toJSONString(), Instance.class);
		} catch (IOException e) {
			log.error("[Param->constructor加载配置出错]", e);
			System.exit(-1);
		}
	}
	
	/**
	 * 从指定的path中读取json字符串
	 * @param path 路径
	 * @return json字符串
	 * @throws IOException 读文件异常
	 */
	private String readJsonFromFile(String path) throws IOException  {
		File file = new File(path);
		byte[] content = new byte[(int)file.length()];
		FileInputStream in = new FileInputStream(file);
		in.read(content);
		in.close();
		return new String(content);
	}
	
	public int getServerPort() {
		return serverPort;
	}
	
	public List<Instance> getRedisInstances() {
		return instances;
	}
	
	public String getStrategy() {
		return strategy;
	}
}
