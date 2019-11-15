package com.study.javaedge.rpc.demo.provider;

import com.study.javaedge.rpc.demo.DemoService;
import com.study.javaedge.rpc.util.PropertiesUtils;
import com.study.javaedge.rpc.common.protocol.JavaSerializeMessageProtocol;
import com.study.javaedge.rpc.server.NettyRpcServer;
import com.study.javaedge.rpc.server.RequestHandler;
import com.study.javaedge.rpc.server.RpcServer;
import com.study.javaedge.rpc.server.register.ServiceObject;
import com.study.javaedge.rpc.server.register.ServiceRegister;
import com.study.javaedge.rpc.server.register.ZookeeperExportServiceRegister;

/**
 * @author JavaEdge
 */
public class Provider {

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(PropertiesUtils.getProperties("rpc.port"));
        String protocol = PropertiesUtils.getProperties("rpc.protocol");

        // 服务注册(ZooKeeper实现)
        ServiceRegister sr = new ZookeeperExportServiceRegister();
        // 实例化服务对象
        DemoService ds = new DemoServiceImpl();
        // 包装成Service对象
        ServiceObject so = new ServiceObject(DemoService.class.getName(), DemoService.class, ds);
        // 注册到服务中心
        sr.register(so, protocol, port);

        RequestHandler reqHandler = new RequestHandler(new JavaSerializeMessageProtocol(), sr);

        RpcServer server = new NettyRpcServer(port, protocol, reqHandler);
        server.start();
        // 按任意键退出
        System.in.read();
        server.stop();
    }
}
