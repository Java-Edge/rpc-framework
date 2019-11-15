package com.study.javaedge.rpc.client.net;

import com.study.javaedge.rpc.discovery.ServiceInfo;

public interface NetClient {
	byte[] sendRequest(byte[] data, ServiceInfo sinfo) throws Throwable;
}
