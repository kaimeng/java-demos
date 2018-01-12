package com.meng.demo.zookeeper.route;

import java.util.List;

public class ServiceInfo {

    private String serviceName;
    private List<String> methods;
    public ServiceInfo() {
    }

    public ServiceInfo(String serviceName, List<String> methods) {
        this.serviceName = serviceName;
        this.methods = methods;
    }
    //get and set


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}
