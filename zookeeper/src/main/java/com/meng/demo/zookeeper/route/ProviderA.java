package com.meng.demo.zookeeper.route;

import com.google.common.collect.Lists;
import com.meng.demo.zookeeper.ZkConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

public class ProviderA {

    public static void main(String[] args) throws Exception {
        //简单演示启动服务
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        //2 通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString(ZkConstants.CONNECT_ADDR)
                .sessionTimeoutMs(ZkConstants.SESSION_OUTTIME)
                .retryPolicy(retryPolicy)
                .build();
        try {
            cf.start();
            //启动服务A
            new Provider(new ServiceInfo("service-A", Lists.newArrayList("m1", "m2")), cf, "192.168.1.101")
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("输入任何字符串关闭服务器");
            System.in.read();
            CloseableUtils.closeQuietly(cf);
        }
    }
}
