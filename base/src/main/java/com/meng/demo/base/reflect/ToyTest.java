package com.meng.demo.base.reflect;


import static com.meng.demo.base.common.Printer.print;

public class ToyTest {
    static void printInfo(Class cc) {
        print("Class name: " + cc.getName() + ", is interface?[" + cc.isInterface() + "], Simple name: " + cc.getSimpleName());
    }

    public static void main(String[] args) {
        try {
            Class c = Class.forName("com.meng.home.reflect.FancyToy");
            printInfo(c);

            for (Class face : c.getInterfaces()) {
                printInfo(face);
            }

            Class up = c.getSuperclass();
            Object obj = up.newInstance();
            printInfo(obj.getClass());

        } catch (Exception e) {
            print(e.getCause());
        }
    }

}

interface HasBatteries {
}

interface Waterproof {
}

interface Shoots {
}

class Toy {
    //Toy() {}

    Toy(int i) {
    }
}

class FancyToy extends Toy implements HasBatteries, Waterproof, Shoots {
    FancyToy() {
        super(1);
    }
}

