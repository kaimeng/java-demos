package com.meng.demo.base.reflect;


import static com.meng.demo.base.common.Printer.print;

public class SweetShop {
    public static void main(String[] args) {
        print("Inside main");
        new Candy();
//        new Candy();
        print("After creating Candy");
        try {
            Class.forName("com.meng.home.reflect.Gum");
//            Class.forName("com.meng.home.reflect.Gum");
        } catch (ClassNotFoundException e) {
            print("Couldn't find Gum");
        }
        print("After Class.forName(\"Gum\")");
        new Cookie();
        print("After creating Cookie");
    }
}


class Candy {
    static {
        print("Loading Candy");
    }
}

class Gum {
    static {
        print("Loading Gum");
    }
}

class Cookie {
    static {
        print("Loading Cookie");
    }
}
