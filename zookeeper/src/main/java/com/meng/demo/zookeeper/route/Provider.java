package com.meng.demo.zookeeper.route;

import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class Provider {


    private ServiceInfo serviceInfo;
    private CuratorFramework cf = null;
    private String ip;

    public Provider(ServiceInfo serviceInfo, CuratorFramework cf, String ip) {
        this.serviceInfo = serviceInfo;
        this.cf = cf;
        this.ip = ip;
    }

    public void start() throws Exception {
        //根节点路径
        String path = "/configcenter/" + serviceInfo.getServiceName();
        Stat stat = cf.checkExists().forPath(path);
        if (stat == null) {
            //不存在
            cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, JSON.toJSONBytes(serviceInfo));
        }
//        InetAddress addr = InetAddress.getLocalHost();
//        String ip = addr.getHostAddress().toString();
        System.out.println("获取本机ip:" + ip);
        //但是为了测试，这里不使用本机ip，因为只有一台机器...
        cf.create().withMode(CreateMode.EPHEMERAL).forPath(path + "/" + ip);
    }
}
