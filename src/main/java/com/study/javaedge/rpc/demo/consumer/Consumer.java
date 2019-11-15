package com.study.javaedge.rpc.demo.consumer;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import com.study.javaedge.rpc.client.ClientStubProxyFactory;
import com.study.javaedge.rpc.client.net.NettyNetClient;
import com.study.javaedge.rpc.common.protocol.JavaSerializeMessageProtocol;
import com.study.javaedge.rpc.common.protocol.MessageProtocol;
import com.study.javaedge.rpc.demo.DemoService;
import com.study.javaedge.rpc.discovery.ZookeeperServiceInfoDiscoverer;

/**
 * @author JavaEdge
 */
public class Consumer {
	public static void main(String[] args) throws Exception {

		ClientStubProxyFactory cspf = new ClientStubProxyFactory();
		// 设置服务发现者
		cspf.setSid(new ZookeeperServiceInfoDiscoverer());

		// 设置支持的协议
		Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
		supportMessageProtocols.put("javas", new JavaSerializeMessageProtocol());
		cspf.setSupportMessageProtocols(supportMessageProtocols);

		// 设置网络层实现
		cspf.setNetClient(new NettyNetClient());

		// 获取远程服务代理
		DemoService demoService = cspf.getProxy(DemoService.class);
		// 执行远程方法
		String hello = demoService.sayHello("world");
		// 显示调用结果
		System.out.println(hello);

		System.out.println(demoService.multiPoint(new Point(5, 10), 2));
	}
}
