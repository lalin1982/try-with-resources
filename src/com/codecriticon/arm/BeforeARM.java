package com.codecriticon.arm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BeforeARM {

    public static void main(String[] args) {
        BufferedReader buffer = null;
        try {
            String linea;
            buffer = new BufferedReader(new FileReader("D:\\pruebas.txt"));
            while ((linea = buffer.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
