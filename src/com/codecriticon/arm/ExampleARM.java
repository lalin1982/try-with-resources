package com.codecriticon.arm;

import com.codecriticon.arm.autoclosable.Adult;
import com.codecriticon.arm.autoclosable.Car;

public class ExampleARM {

    public static void main(String[] args) {
        try (Adult adult = new Adult(); Car car = new Car()) {
            car.drive();
            adult.work();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("Fin del día.");
        }

    }
}
