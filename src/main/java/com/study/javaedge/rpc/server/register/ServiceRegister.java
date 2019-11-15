package com.study.javaedge.rpc.server.register;

/**
 * @author JavaEdge
 */
public interface ServiceRegister {

	/**
	 * 注册服务
	 *
	 * @param so 服务对象
	 * @param protocol 协议
	 * @param port 端口
	 * @throws Exception
	 */
	void register(ServiceObject so, String protocol, int port) throws Exception;

	/**
	 * 获取服务对象
	 *
	 * @param name 服务名
	 * @return 服务对象
	 * @throws Exception
	 */
	ServiceObject getServiceObject(String name) throws Exception;
}
