package com.meng.demo.base.reflect;


import java.util.Random;

import static com.meng.demo.base.common.Printer.print;

class Initable {
    // 编译期常量
    static final int staticFinal = 47;
    static final int staticFinal2 = ClassInitialization.rand.nextInt(1000);
    static {
        print("初始化 Initable");
    }
}

class Initable2 {
    // 非final，链接和初始化
    static int staticNonFinal = 147;
    static {
        print("初始化 Initable2");
    }
}


class Initable3 {
    static int staticNonFinal = 74;
    static {
        print("初始化 Initable3");
    }
}


public class ClassInitialization {
    public static Random rand = new Random(47);

    public static void main(String[] args) throws Exception {
        Class initable = Initable.class;
        print("创建 Initable 引用之后");
        // 没有触发初始化
        print(Initable.staticFinal);
        print(Initable.staticFinal2);

        // 触发初始化
        print(Initable2.staticNonFinal);

        Class initable3 = Class.forName("com.meng.home.reflect.Initable3");
    }
}
