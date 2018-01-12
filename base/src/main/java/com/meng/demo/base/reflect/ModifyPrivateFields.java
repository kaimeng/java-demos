package com.meng.demo.base.reflect;

import java.lang.reflect.Field;

import static com.meng.demo.base.common.Printer.print;

class WithPrivateFinalField {
    private int i = 1;
    private final String s = "I'm totally safe";
    private String s2 = "Am I safe?";

    public String toString() {
        return "i = " + i + ", " + s + ", " + s2;
    }
}

public class ModifyPrivateFields {
    public static void main(String[] args) throws Exception {
        WithPrivateFinalField pf = new WithPrivateFinalField();
        print(pf);

        // 获取设置私有域
        print("private ------");
        Field f = pf.getClass().getDeclaredField("i");
        f.setAccessible(true);
        print("f.getInt(pf): " + f.getInt(pf));
        f.setInt(pf, 47);
        print(pf);

        // 获取设置私有 final 域
        print("\nprivate final ------");
        f = pf.getClass().getDeclaredField("s");
        f.setAccessible(true);
        print("f.get(pf): " + f.get(pf));
        f.set(pf, "No, you are not safe");
        print(pf);

        print("\nprivate ------");
        f = pf.getClass().getDeclaredField("s2");
        f.setAccessible(true);
        print("f.get(pf):" + f.get(pf));
        f.set(pf, "No, you ara ...");
        print(pf);

    }
}
