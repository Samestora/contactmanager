/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbo.contactmanager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

// Data Access Object
public class ContactDAO {
    private Connection conn;
    public boolean New = false;
    
    public ContactDAO() throws SQLException{
        // Create database if not exists
        String url = "jdbc:mysql://localhost:3306?useSSL=false";
        String user = "root";
        String pass = "";
        conn = DriverManager.getConnection(url, user, pass);
        
        if (initDB() == false){
            New = true;
        }
        
        // Init table anyways
        url = "jdbc:mysql://localhost:3306/contact_manager?useSSL=false";
        conn = DriverManager.getConnection(url, user, pass);
        initIfBlank();
    }
    
    private void executeSqlFile(Connection connection, String filePath) {
        try (
                InputStream inputStream = getClass().getResourceAsStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                Statement statement = connection.createStatement()
            ) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line);
                if (line.endsWith(";")) { // Execute each statement
                    statement.execute(sqlBuilder.toString());
                    sqlBuilder.setLength(0); // Reset for the next statement
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean initDB() throws SQLException {
        String createDBSQL = "CREATE DATABASE contact_manager;"; // Intended to make an error
        try (Statement stmt = conn.createStatement()){
            stmt.execute(createDBSQL);
            return false;
        } catch (SQLException ex) {
            return true; // It exists hooray
        }
    }
    private void initIfBlank() throws SQLException{
        executeSqlFile(conn, "/sql/contacts.sql");
    }
    
    public void initDummy() throws SQLException {
        executeSqlFile(this.conn, "/sql/dummy.sql");
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
