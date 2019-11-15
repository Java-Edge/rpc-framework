package com.study.javaedge.rpc.discovery;

import java.util.List;

/**
 * 服务发现接口
 *
 * @author JavaEdge
 * @date 2019/11/15
 */
public interface ServiceInfoDiscoverer {

	/**
	 * 获取服务信息
	 *
	 * @param name 服务名
	 * @return 服务信息列表
	 */
	List<ServiceInfo> getServiceInfo(String name);
}
