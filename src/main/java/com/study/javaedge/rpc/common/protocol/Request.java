package com.study.javaedge.rpc.common.protocol;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求
 *
 * @author JavaEdge
 * @date 2019/11/15
 */
@Getter
@Setter
public class Request implements Serializable {

	private static final long serialVersionUID = -5200571424236772650L;

	private String serviceName;

	private String method;

	private Map<String, String> headers = new HashMap<String, String>();

	private Class<?>[] paramTypes;

	private Object[] parameters;
}
