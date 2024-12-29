package com.ashbank.main;

import com.ashbank.objects.utility.Security;

public class TestsMain {
    public static void main(String[] args) {
        Security security = new Security();

        String password = "admin123";
        String hashedPassword = security.hashSecurityData(password);

        System.out.println("Hashed password:\t" + hashedPassword);

//        for (int i = 0; i < 6; ++i)
//            System.out.printf("ID [%d]: %s%n\t", (i+1), security.generateUUID() );
    }
}
