package com.study.javaedge.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.study.javaedge.rpc.client.net.NetClient;
import com.study.javaedge.rpc.common.protocol.MessageProtocol;
import com.study.javaedge.rpc.common.protocol.Request;
import com.study.javaedge.rpc.common.protocol.Response;
import com.study.javaedge.rpc.discovery.ServiceInfo;
import com.study.javaedge.rpc.discovery.ServiceInfoDiscoverer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JavaEdge
 * @date 2019/11/15
 */
@Getter
@Setter
public class ClientStubProxyFactory {

    private ServiceInfoDiscoverer sid;

    private Map<String, MessageProtocol> supportMessageProtocols;

    private NetClient netClient;

    private Map<Class<?>, Object> objectCache = new HashMap<>();

    public <T> T getProxy(Class<T> interf) {
        T obj = (T) this.objectCache.get(interf);
        if (obj == null) {
            obj = (T) Proxy.newProxyInstance(interf.getClassLoader(), new Class<?>[]{interf},
                    new ClientStubInvocationHandler(interf));
            this.objectCache.put(interf, obj);
        }

        return obj;
    }

    private class ClientStubInvocationHandler implements InvocationHandler {
        private Class<?> interf;

        private Random random = new Random();

        public ClientStubInvocationHandler(Class<?> interf) {
            super();
            this.interf = interf;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if ("toString".equals(method.getName())) {
                return proxy.getClass().toString();
            }

            if ("hashCode".equals(method.getName())) {
                return 0;
            }

            // 1、获得服务信息(接口)
            String serviceName = this.interf.getName();
            List<ServiceInfo> serviceInfo = sid.getServiceInfo(serviceName);

            if (serviceInfo == null || serviceInfo.size() == 0) {
                throw new Exception("远程服务不存在！");
            }

            // 随机选择一个服务提供者（软负载均衡）
            ServiceInfo serviceInfo1 = serviceInfo.get(random.nextInt(serviceInfo.size()));

            // 2、构造request对象, 要调用的方法
            Request req = new Request();
            req.setServiceName(serviceInfo1.getName());
            req.setMethod(method.getName());
            req.setParamTypes(method.getParameterTypes());
            req.setParameters(args);

            // 3、协议层编组
            // 获得该方法对应的协议
            MessageProtocol protocol = supportMessageProtocols.get(serviceInfo1.getProtocol());
            // 编组请求
            byte[] data = protocol.marshallingRequest(req);

            // 4、调用网络层发送请求
            byte[] repData = netClient.sendRequest(data, serviceInfo1);

            // 5解组响应消息
            Response response = protocol.unmarshallingResponse(repData);

            // 6、结果处理
            if (response.getException() != null) {
                throw response.getException();
            }

            return response.getReturnValue();
        }
    }
}
