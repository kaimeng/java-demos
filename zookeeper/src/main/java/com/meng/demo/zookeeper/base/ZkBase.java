package com.meng.demo.zookeeper.base;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created by carl.yu on 2016/11/22.
 */
public class ZkBase {
    static final String CONNECT_ADDR = "spmaster.bigdata.ly:22222,spslave1.bigdata.ly:22222,spslave2.bigdata.ly:22222,spslave3.bigdata.ly:22222,spslave4.bigdata.ly:22222";
    static final int SESSION_TIMEOUT = 2000;
    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                //获取事件的状态
                Event.KeeperState keeperState = event.getState();
                Event.EventType eventType = event.getType();
                //如果是建立连接
                if (Event.KeeperState.SyncConnected == keeperState) {
                    if (Event.EventType.None == eventType) {
                        //如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        connectedSemaphore.countDown();
                        System.out.println("zk 建立连接");
                    }
                }
            }
        });
        //创建持久节点
        System.out.println("创建节点/testRoot");
        zk.create("/testRoot", "testRoot".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //创建子节点
        System.out.println("创建子节点/testRoot/children");
        zk.create("/testRoot/children", "children data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        //获取节点信息
        byte[] data = zk.getData("/testRoot", false, null);
        System.out.println("获取节点信息");
        System.out.println(new String(data));
        System.out.println(zk.getChildren("/testRoot", false));

        //修改节点的值
        System.out.println("修改节点的值");
        zk.setData("/testRoot", "modify data root".getBytes(), -1);
        data = zk.getData("/testRoot", false, null);
        System.out.println(new String(data));

        //判断节点是否存在
        System.out.println(zk.exists("/testRoot/children", false));
        //删除节点
        System.out.println("删除节点");
        zk.delete("/testRoot/children", -1);
        System.out.println(zk.exists("/testRoot/children", false));
        //进行阻塞
        connectedSemaphore.await();
        zk.close();
        System.out.println("断开了连接");
    }
}
