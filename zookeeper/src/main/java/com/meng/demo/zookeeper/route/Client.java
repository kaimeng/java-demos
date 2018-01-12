package com.meng.demo.zookeeper.route;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meng.demo.zookeeper.ZkConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Map;

public class Client {

    private CuratorFramework cf = null;
    //本地保存了数据信息
    private Map<String, List<String>> data = Maps.newHashMap();


    public Client() {

    }

    public void start() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        this.cf = CuratorFrameworkFactory.builder()
                .connectString(ZkConstants.CONNECT_ADDR)
                .sessionTimeoutMs(ZkConstants.SESSION_OUTTIME)
                .retryPolicy(retryPolicy)
                .build();
        this.cf.start();

        try {
            subscribe(new ServiceInfo(
                    "service-A",
                    Lists.newArrayList("m1", "m2")
            ));
            subscribe(new ServiceInfo(
                    "service-B",
                    Lists.newArrayList("m3", "m4")
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        CloseableUtils.closeQuietly(cf);
    }



    public void subscribe(final ServiceInfo serviceInfo) throws Exception {
        String serviceName = serviceInfo.getServiceName();
        //订阅某一个服务
        final String path = "/configcenter/" + serviceName;
        System.out.println("path:" + path);
        Stat stat = cf.checkExists().forPath(path);
        if (stat == null) {
            System.out.println(serviceName + "没有服务");
            cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, JSON.toJSONBytes(serviceInfo));
        }
        PathChildrenCache cache = new PathChildrenCache(cf, path, true);
        //5 在初始化的时候就进行缓存监听
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((CuratorFramework client, PathChildrenCacheEvent event) -> {
            List<String> children = client.getChildren().forPath(path);
            data.put(serviceName, children);
            System.out.println(String.format("%s发生了改变:%s", serviceName, children.toString()));
        });
    }

}
