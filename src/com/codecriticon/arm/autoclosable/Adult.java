package com.codecriticon.arm.autoclosable;

public class Adult implements AutoCloseable{

    public Adult() {
        System.out.println("Me levanto por la mañana");
    }

    public void work() {
        System.out.println("Hago mi trabajo");
    }

    @Override
    public void close() throws Exception {
        System.out.println("Me voy a dormir");
    }
}
