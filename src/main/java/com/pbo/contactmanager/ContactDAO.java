/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbo.contactmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author LENOVO
 */
public class ContactDAO {
    private Connection conn;
    
    public ContactDAO() throws SQLException{
        // Create database if not exists
        String url = "jdbc:mysql://localhost:3306?useSSL=false";
        String user = "root";
        String pass = "";
        conn = DriverManager.getConnection(url, user, pass);
        initDB();
        
        // Init table anyways
        url = "jdbc:mysql://localhost:3306/contact_manager?useSSL=false";
        conn = DriverManager.getConnection(url, user, pass);
        initIfBlank();
    }
    private void initDB() throws SQLException {
        String createDBSQL = "CREATE DATABASE IF NOT EXISTS contact_manager;";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(createDBSQL);
        }
    }
    private void initIfBlank() throws SQLException{
        String createTableSQL = "CREATE TABLE IF NOT EXISTS contacts (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "name VARCHAR(100), " +
                                "phone VARCHAR(15), " +
                                "email VARCHAR(100), " +
                                "address VARCHAR(255))";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(createTableSQL);   
        }
    }
    
    // Real Methods!
    public void addContact(String name, String phone, String email, String address) throws SQLException {
        String insertContact = "INSERT INTO contacts (name, phone, email, address) VALUES (?,?,?,?)";
        try(PreparedStatement pstmt = conn.prepareStatement(insertContact)){
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, address);
            pstmt.executeUpdate();
        }
    }
    
    public void deleteContact(int id) throws SQLException {
        String deleteContact = "DELETE FROM contacts WHERE id=?";
        try(PreparedStatement pstmt = conn.prepareStatement(deleteContact)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
        
    }
    //  @Update all but give placeholder so it's need to be changed
    public void updateContact(int id, String name, String phone, String email, String address) throws SQLException {
        String updateSQL = "UPDATE contacts SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(updateSQL)){
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, address);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        }

    }
    
    // get all (init display)
    public ResultSet getAllContact() throws SQLException {
        String selectAll = "SELECT * FROM contacts";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(selectAll);
    }
}
