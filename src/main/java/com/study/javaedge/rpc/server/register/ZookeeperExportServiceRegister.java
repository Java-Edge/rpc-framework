package com.study.javaedge.rpc.server.register;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;

import com.study.javaedge.rpc.discovery.ServiceInfo;
import com.study.javaedge.rpc.util.PropertiesUtils;
import org.I0Itec.zkclient.ZkClient;

import com.alibaba.fastjson.JSON;

/**
 * Zookeeper方式获取远程服务信息类。
 * <p>
 * ZookeeperServiceInfoDiscoverer
 *
 * @author JavaEdge
 */
public class ZookeeperExportServiceRegister extends DefaultServiceRegister implements ServiceRegister {

    private ZkClient client;

    private String centerRootPath = "/Rpc-framework";

    public ZookeeperExportServiceRegister() {
        String addr = PropertiesUtils.getProperties("zk.address");
        client = new ZkClient(addr);
        client.setZkSerializer(new MyZkSerializer());
    }

    @Override
    public void register(ServiceObject so, String protocolName, int port) throws Exception {
        super.register(so, protocolName, port);

        ServiceInfo serviceInfo = new ServiceInfo();

        String host = InetAddress.getLocalHost().getHostAddress();
        String address = host + ":" + port;
        serviceInfo.setAddress(address);
        serviceInfo.setName(so.getInterf().getName());
        serviceInfo.setProtocol(protocolName);
        this.exportService(serviceInfo);

    }

    /**
     * 创建节点
     *
     * @param serviceResource
     */
    private void exportService(ServiceInfo serviceResource) {
        String serviceName = serviceResource.getName();
        // 转为JSON串
        String uri = JSON.toJSONString(serviceResource);
        try {
            uri = URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String servicePath = centerRootPath + "/" + serviceName + "/service";
        if (!client.exists(servicePath)) {
            // 创建临时节点
            client.createPersistent(servicePath, true);
        }
        String uriPath = servicePath + "/" + uri;
        if (client.exists(uriPath)) {
            client.delete(uriPath);
        }
        // 创建真正的节点
        client.createEphemeral(uriPath);
    }
}
