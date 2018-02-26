package com.meng.demo.zookeeper.loadbalance.zksimple;

import com.meng.demo.zookeeper.ZkConstants;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class SimpleClient {

    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private static List<String> servers = new ArrayList<>();

    public static void main(String[] args) {

        initServerList();

        SimpleClient client = new SimpleClient();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String name;
            try {
                name = console.readLine();
                if("exit".equals(name)) {
                    System.exit(0);
                }
                client.send(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initServerList() {
        try {
            //启动时从ZooKeeper读取可用服务器
            String path = "/test";
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



            List<String> childs = zk.getChildren(path, false);
            servers.clear();
            for (String p : childs) {
                zk.getChildren(path, false);
                byte[] data = zk.getData(path + "/" + p, false, null);
                servers.add(new String(data));
            }



            //订阅节点变化事件
            zk.getChildren(path, event -> {
                try {
                    if (event.getType() != Watcher.Event.EventType.NodeDeleted) {

                        List<String> currentChilds = zk.getChildren(path, false);
                        System.out.println("current: " + currentChilds);
                        servers.clear();
                        for (String p : currentChilds) {
                            byte[] data = zk.getData(path + "/" + p, false, null);
                            servers.add(new String(data));
                        }
                        System.out.println("Servers: " + servers.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    public static String getServer() {
        return servers.get(new Random().nextInt(servers.size()));
    }

    public SimpleClient() {
    }

    public void send(String name) {

        String server = com.meng.demo.zookeeper.loadbalance.simple.SimpleClient.getServer();
        String[] cfg = server.split(":");

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket(cfg[0], Integer.parseInt(cfg[1]));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(name);
            while(true) {
                String resp = in.readLine();
                if(resp == null)
                    break;
                else if(resp.length() > 0) {
                    System.out.println("Receive : " + resp);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

