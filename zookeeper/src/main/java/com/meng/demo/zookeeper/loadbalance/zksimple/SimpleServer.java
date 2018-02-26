package com.meng.demo.zookeeper.loadbalance.zksimple;

import com.meng.demo.zookeeper.ZkConstants;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class SimpleServer implements Runnable {

    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        int port = 18080;
        SimpleServer server = new SimpleServer(port);
        Thread thread = new Thread(server);
        thread.start();
    }

    private int port;

    public SimpleServer(int port) {
        this.port = port;
    }

    private void regServer() {
        try {
            //向ZooKeeper注册当前服务器
            ZooKeeper zk = new ZooKeeper(ZkConstants.CONNECT_ADDR, ZkConstants.SESSION_OUTTIME, event -> {
                //获取事件的状态
                Watcher.Event.KeeperState keeperState = event.getState();
                Watcher.Event.EventType eventType = event.getType();
                //如果是建立连接
                if (Watcher.Event.KeeperState.SyncConnected == keeperState) {
                    if (Watcher.Event.EventType.None == eventType) {
                        //如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        connectedSemaphore.countDown();
                        System.out.println("zk 建立连接");
                    }
                }
            });

            String bastPath = "/test";
            //判断节点是否存在
            if (zk.exists(bastPath, false) != null) {
                zk.delete(bastPath, -1);
            }

            String path = "/test/server" + port;

            //判断节点是否存在
            if (zk.exists(path, false) != null) {
                zk.delete(path, -1);
            }
            zk.create(path, ("127.0.0.1:" + port).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            regServer();
            System.out.println("Server started at " + port);
            Socket socket;
            while (true) {
                socket = server.accept();
                new Thread(new SimpleServerHandler(socket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                }
            }
        }

    }
}


