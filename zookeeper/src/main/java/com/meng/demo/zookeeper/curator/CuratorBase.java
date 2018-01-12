package com.meng.demo.zookeeper.curator;

import com.meng.demo.zookeeper.ZkConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CuratorBase {



    public static void main(String[] args) {
        //1 重试策略：初试时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        //2 通过工厂创建连接
        CuratorFramework cf = null;
        try {
            cf = CuratorFrameworkFactory.builder()
                    .connectString(ZkConstants.CONNECT_ADDR)
                    .sessionTimeoutMs(ZkConstants.SESSION_OUTTIME)
                    .retryPolicy(retryPolicy)
//                    .namespace("super")
                    .build();
            //3 开启连接
            cf.start();
            //4 建立节点 指定节点类型（不加withMode默认为持久类型节点）、路径、数据内容
//            cf.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/super_/c1", "c1内容".getBytes());
            //5 删除节点
            cf.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");
            // 读取、修改

            /*创建节点*/
            cf.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/super/c1", "c1内容".getBytes());
            cf.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/super/c2", "c2内容".getBytes());
            /*读取节点*/
            String ret1 = new String(cf.getData().forPath("/super/c2"));
            System.out.println(ret1);
            /*修改节点*/
            cf.setData().forPath("/super/c2", "修改c2内容".getBytes());
            String ret2 = new String(cf.getData().forPath("/super/c2"));
            System.out.println(ret2);

            // 绑定回调函数
            ExecutorService pool = Executors.newCachedThreadPool();
            cf.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                    .inBackground((cf1, ce) -> {
                        System.out.println("code:" + ce.getResultCode());
                        System.out.println("type:" + ce.getType());
                        System.out.println("线程为:" + Thread.currentThread().getName());
                        System.out.println("path: " + ce.getPath());
                    }, pool)
                    .forPath("/super/c3", "c3内容".getBytes());
            Thread.sleep(Integer.MAX_VALUE);


            // 读取子节点getChildren方法 和 判断节点是否存在checkExists方法
            /**
             List<String> list = cf.getChildren().forPath("/super");
             for(String p : list){
             System.out.println(p);
             }

             Stat stat = cf.checkExists().forPath("/super/c3");
             System.out.println(stat);

             Thread.sleep(2000);
             cf.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");
             */


            //cf.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭连接");
            CloseableUtils.closeQuietly(cf);
        }
    }
}
