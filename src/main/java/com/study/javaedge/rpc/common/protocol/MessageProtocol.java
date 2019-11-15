package com.study.javaedge.rpc.common.protocol;

/**
 * 消息的序列化与反序列化 - 接口
 *
 * @author JavaEdge
 * @date 2019/11/15
 */
public interface MessageProtocol {

	/**
	 * 对整个请求序列化
	 *
	 * @param req
	 * @return
	 * @throws Exception
	 */
	byte[] marshallingRequest(Request req) throws Exception;

	Request unmarshallingRequest(byte[] data) throws Exception;

	byte[] marshallingResponse(Response rsp) throws Exception;

	Response unmarshallingResponse(byte[] data) throws Exception;
}
