package com.study.javaedge.rpc.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.study.javaedge.rpc.common.protocol.MessageProtocol;
import com.study.javaedge.rpc.common.protocol.Request;
import com.study.javaedge.rpc.common.protocol.Response;
import com.study.javaedge.rpc.common.protocol.Status;
import com.study.javaedge.rpc.server.register.ServiceObject;
import com.study.javaedge.rpc.server.register.ServiceRegister;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JavaEdge
 */
@Setter
@Getter
public class RequestHandler {
	private MessageProtocol protocol;

	private ServiceRegister serviceRegister;

	public RequestHandler(MessageProtocol protocol, ServiceRegister serviceRegister) {
		super();
		this.protocol = protocol;
		this.serviceRegister = serviceRegister;
	}

	public byte[] handleRequest(byte[] data) throws Exception {
		// 1、解组消息
		Request req = this.protocol.unmarshallingRequest(data);

		// 2、查找服务对象
		ServiceObject so = this.serviceRegister.getServiceObject(req.getServiceName());

		Response rsp;

		if (so == null) {
			rsp = new Response(Status.NOT_FOUND);
		} else {
			// 3、反射调用对应的过程方法
			try {
				Method m = so.getInterf().getMethod(req.getMethod(), req.getParamTypes());
				Object returnValue = m.invoke(so.getObj(), req.getParameters());
				rsp = new Response(Status.SUCCESS);
				rsp.setReturnValue(returnValue);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				rsp = new Response(Status.ERROR);
				rsp.setException(e);
			}
		}

		// 4、编组响应消息
		return this.protocol.marshallingResponse(rsp);
	}
}
