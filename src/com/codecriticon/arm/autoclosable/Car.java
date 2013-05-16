package com.codecriticon.arm.autoclosable;

public class Car implements AutoCloseable{

    public Car() {
        System.out.println("Coche aparcado en el garaje");
    }

    public void drive() {
        System.out.println("Voy al trabajo con el coche");
    }

    @Override
    public void close() throws Exception {
        System.out.println("Aparco el coche en el garaje");
    }
}
