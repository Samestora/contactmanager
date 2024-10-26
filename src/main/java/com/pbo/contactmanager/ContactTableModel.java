/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbo.contactmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO
 */
public class ContactTableModel {
    public static DefaultTableModel buildTable(ResultSet resultset) throws SQLException {
        String[] columnnames = {"ID", "Names", "Phone", "Email", "Address" };
        
        DefaultTableModel model = new DefaultTableModel(columnnames, 0);
        
        while (resultset.next()) {
            Object[] row = {
                resultset.getInt("id"),
                resultset.getString("name"),
                resultset.getString("phone"),
                resultset.getString("email"),
                resultset.getString("address"),
            };
            model.addRow(row);
        }
        
        return model;
    }
}
