package com.study.javaedge.rpc.server.register;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JavaEdge
 */
public class DefaultServiceRegister implements ServiceRegister {

	private Map<String, ServiceObject> serviceMap = new HashMap<>();

	@Override
	public void register(ServiceObject so, String protocolName, int port) throws Exception {
		if (so == null) {
			throw new IllegalArgumentException("参数不能为空");
		}

		this.serviceMap.put(so.getName(), so);
	}

	@Override
	public ServiceObject getServiceObject(String name) {
		return this.serviceMap.get(name);
	}

}
