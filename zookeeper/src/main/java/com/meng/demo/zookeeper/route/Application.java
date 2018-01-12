package com.meng.demo.zookeeper.route;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) throws Exception {
        ScheduledThreadPoolExecutor monitorService = new ScheduledThreadPoolExecutor(10,
                new ThreadFactoryBuilder().setNameFormat("lifecycleSupervisor-%d").build());
        monitorService.setMaximumPoolSize(20);
        monitorService.setKeepAliveTime(30, TimeUnit.SECONDS);

        Client client = new Client();

        ScheduledFuture<?> future = monitorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                client.start();

            }
        }, 0, 3, TimeUnit.SECONDS);

        client.close();

    }
}
