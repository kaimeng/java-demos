package com.meng.demo.base.reflect;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static com.meng.demo.base.common.Printer.print;

interface Interface{
    void doSomething();
    void somethingElse(String args);
}

class RealObject implements Interface {
    @Override
    public void doSomething() {
        print("doSomething");
    }

    @Override
    public void somethingElse(String args) {
        print("somethingElse " + args);
    }
}

/**
 * 简单代理
 */
class SimpleProxy implements Interface {
    private Interface proxied;

    public SimpleProxy(Interface proxied) {
        this.proxied = proxied;
    }

    @Override
    public void doSomething() {
        print("doSomething -- proxy");
        proxied.doSomething();
    }

    @Override
    public void somethingElse(String args) {
        print("somethingElse -- proxy");
        proxied.somethingElse(args);
    }
}

/**
 * Java的代理
 */
class DynamicProxyHandler implements InvocationHandler {
    private Object proxied;

    public DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        print("**** proxy: " + proxy.getClass() + ", method: " + method + ", args: " + args);
        if (args != null) {
            for (Object arg : args) {
                print("  " + arg);
            }
        }
        return method.invoke(proxied, args);
    }
}

public class SimpleDynamicProxy {
    public static void consumer(Interface iface) {
        iface.doSomething();
        iface.somethingElse("bonobo");
    }

    public static void main(String[] args)  {
        RealObject real = new RealObject();
        consumer(real);

        consumer(new SimpleProxy(real));

        Interface proxy = (Interface) Proxy.newProxyInstance(Interface.class.getClassLoader(), new Class[]{Interface.class}, new DynamicProxyHandler(real));
        consumer(proxy);
    }
}
