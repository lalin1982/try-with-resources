package com.codecriticon.arm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WithARM {

    public static void main(String[] args) {

        try (BufferedReader buffer = new BufferedReader(new FileReader("D:\\pruebas.txt"))) {
            String linea;
            while ((linea = buffer.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
