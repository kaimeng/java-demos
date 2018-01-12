package com.meng.demo.base.reflect;

public class ClassReferences {
    public static void main(String[] args) {
        // 所有类型
        Class intClass = int.class;
        intClass = double.class;

        // 整型
        Class<Integer> genericIntClass = int.class;
//        genericIntClass = double.class;

        // 泛型
        Class<?> intClass2 = int.class;
        intClass2 = double.class;

        Class<? extends Number> numberClass = int.class;
        numberClass = double.class;

    }
}
