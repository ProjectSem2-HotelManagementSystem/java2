package com.example.database;

import com.example.database.DbSet;
import com.example.database.IDbSet;
import com.example.exceptions.AlertMessage;
import com.example.model.Employee;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbSetEmployee extends DbSet implements IDbSet<Employee> {
    AlertMessage alert = new AlertMessage();
    ArrayList<Employee> listItems = new ArrayList<Employee>();
    PreparedStatement ps;
    public ArrayList<Employee> Register(String username, String email, String password) {
        try {
            ps = PrepareStatement("SELECT * FROM admin WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                alert.errorMessage(username + " is already exists!");
            } else {
                ps = PrepareStatement("INSERT INTO admin (email, username, password) VALUES (?, ?, ?)");
                ps.setString(1, email);
                ps.setString(2, username);
                ps.setString(3, password);

                ps.executeUpdate();

                alert.successMessage("Registered Successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listItems;
    }

    public ArrayList<Employee> Login(String username, String password) {
        try {
            ps = PrepareStatement("SELECT * FROM admin WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                alert.successMessage("OK");
            } else {
                alert.errorMessage("Incorrect Username/Password");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listItems;
    }

}
