package com.study.javaedge.rpc.server;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JavaEdge
 */
@Getter
@Setter
public abstract class RpcServer {

	protected int port;

	protected String protocol;

	protected RequestHandler handler;

	public RpcServer(int port, String protocol, RequestHandler handler) {
		super();
		this.port = port;
		this.protocol = protocol;
		this.handler = handler;
	}

	/**
	 * 开启服务
	 */
	public abstract void start();

	/**
	 * 停止服务
	 */
	public abstract void stop();
}
