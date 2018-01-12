package com.meng.demo.zookeeper.curator;

import com.meng.demo.zookeeper.ZkConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

public class CuratorWatcher1 {


    public static void main(String[] args) throws Exception {

        //1 重试策略：初试时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        //2 通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString(ZkConstants.CONNECT_ADDR)
                .sessionTimeoutMs(ZkConstants.SESSION_OUTTIME)
                .retryPolicy(retryPolicy)
                .build();

        //3 建立连接
        cf.start();


        cf.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");


        //4 建立一个cache缓存
        final NodeCache cache = new NodeCache(cf, "/super", false);
        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            /**
             * <B>方法名称：</B>nodeChanged<BR>
             * <B>概要说明：</B>触发事件为创建节点和更新节点，在删除节点的时候并不触发此操作。<BR>
             * @see org.apache.curator.framework.recipes.cache.NodeCacheListener#nodeChanged()
             */
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("路径为：" + cache.getCurrentData().getPath());
                System.out.println("数据为：" + new String(cache.getCurrentData().getData()));
                System.out.println("状态为：" + cache.getCurrentData().getStat());
                System.out.println("---------------------------------------");
            }
        });
        Thread.sleep(1000);
        cf.create().forPath("/super", "123".getBytes());
        Thread.sleep(1000);
        cf.setData().forPath("/super", "456".getBytes());

        Thread.sleep(1000);
        cf.delete().forPath("/super");
        Thread.sleep(1000);
        System.out.println("按下任何键表示关闭...");
        System.in.read();

        CloseableUtils.closeQuietly(cache);
        CloseableUtils.closeQuietly(cf);
    }
}
