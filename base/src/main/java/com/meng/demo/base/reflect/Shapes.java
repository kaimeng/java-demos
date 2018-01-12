package com.meng.demo.base.reflect;

import java.util.Arrays;
import java.util.List;

import static com.meng.demo.base.common.Printer.print;

public class Shapes {
    public static void main(String[] args) {
        List<Shape> shapeList = Arrays.asList(new Circle(), new Square(), new Triangle());
        shapeList.forEach(shape -> shape.draw());
    }
}

abstract class Shape {
    void draw() {
        print(this + ".draw()");
    }

    abstract public String toString();
}

class Circle extends Shape {
    @Override
    public String toString() {
        return "Circle";
    }
}

class Square extends Shape {
    @Override
    public String toString() {
        return "Square";
    }
}

class Triangle extends Shape {
    @Override
    public String toString() {
        return "Triangle";
    }
}