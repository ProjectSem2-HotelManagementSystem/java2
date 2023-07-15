package com.example.projectsem2;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection ConnectDB() {
        try{
            Class.forName("org.postgresql.Driver");
            Connection connect = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/HotelManagement",
                            "postgres", "admin123");
            System.out.println("Opened database successfully");
            return connect;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
