package com.study.javaedge.rpc.discovery;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.study.javaedge.rpc.server.register.MyZkSerializer;
import com.study.javaedge.rpc.util.PropertiesUtils;
import org.I0Itec.zkclient.ZkClient;

import com.alibaba.fastjson.JSON;

/**
 * ZooKeeper 实现服务的发现
 *
 * @author JavaEdge
 * @date 2019/11/15
 */
public class ZookeeperServiceInfoDiscoverer implements ServiceInfoDiscoverer {

	ZkClient client;

	private String centerRootPath = "/Rpc-framework";

	public ZookeeperServiceInfoDiscoverer() {
		String addr = PropertiesUtils.getProperties("zk.address");
		client = new ZkClient(addr);
		client.setZkSerializer(new MyZkSerializer());
	}

	@Override
	public List<ServiceInfo> getServiceInfo(String name) {
		String servicePath = centerRootPath + "/" + name + "/service";
		List<String> children = client.getChildren(servicePath);
		List<ServiceInfo> resources = new ArrayList<ServiceInfo>();
		for (String ch : children) {
			try {
				String deCh = URLDecoder.decode(ch, "UTF-8");
				ServiceInfo r = JSON.parseObject(deCh, ServiceInfo.class);
				resources.add(r);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return resources;
	}

}
