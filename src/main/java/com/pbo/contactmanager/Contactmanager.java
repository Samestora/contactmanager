/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.pbo.contactmanager;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author LENOVO
 */
public class Contactmanager extends JFrame{
    private JTextField nameField, phoneField, emailField, addressField;
    private JButton addButton, updateButton, deleteButton;
    private JTable contactTable;
    private ContactDAO contactDAO;
    
    public Contactmanager(){
         try {
            contactDAO = new ContactDAO(); // Ensure this doesn't throw an exception
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;  // Exit if DB connection fails
        }
         
        setTitle("Contact Manager Community");
        setSize(1600, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Center
        setLocationRelativeTo(null);
        
         // Creating input fields
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressField = new JTextField(20);
        
        // Creating buttons
        addButton = new JButton("Add Contact");
        updateButton = new JButton("Update Contact");
        deleteButton = new JButton("Delete Contact");
        
        // Setting up the table to display contacts
        contactTable = new JTable();
        
         // Layout
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        
        JScrollPane tablePane = new JScrollPane(contactTable);
        
        add(panel, BorderLayout.NORTH);
        add(tablePane, BorderLayout.CENTER);
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input from text fields
                String name = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String address = addressField.getText();

                // Validate input (ensure no fields are empty)
                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(Contactmanager.this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Call DAO to add contact
                try {
                    contactDAO.addContact(name, phone, email, address);
                    JOptionPane.showMessageDialog(Contactmanager.this, "Contact added successfully!");
                    refreshContactTable();  // Refresh table after adding contact
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(Contactmanager.this, "Error adding contact.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }

                // Clear input fields after adding
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                addressField.setText("");
               }
           });

        updateButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    int selectedRow = contactTable.getSelectedRow();
                    if(selectedRow == -1) {
                        JOptionPane.showMessageDialog(Contactmanager.this, "Please select a contact to update.", "Select Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    int contactId = getSelectedContactId(selectedRow);
                    
                    String name = nameField.getText();
                    String phone = phoneField.getText();
                    String email = emailField.getText();
                    String address = addressField.getText();
                    
                    //Validate input (pastikan tidak ada kolom yang kosong)
                    if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                        JOptionPane.showMessageDialog(Contactmanager.this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    //Call DAO to update contact
                    try {
                        contactDAO.updateContact(contactId, name, phone, email, address);
                        JOptionPane.showMessageDialog(Contactmanager.this, "Contact updated successfully!");
                        refreshContactTable();
                    } catch (SQLException ex){
                        JOptionPane.showMessageDialog(Contactmanager.this, "Error updating contact.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    //Clear input fields after updating
                    nameField.setText("");
                    phoneField.setText("");
                    emailField.setText("");
                    addressField.setText("");
                    }
                });
        
        deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = contactTable.getSelectedRow();
                        if (selectedRow == -1) {
                            JOptionPane.showMessageDialog(Contactmanager.this, "Please select a contact to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        
                        int contactId = getSelectedContactId(selectedRow);
                        
                        int confirm = JOptionPane.showConfirmDialog(Contactmanager.this, "Are you sure you want to delete this contact?", "Confirm Delete", JOptionPane.YES_OPTION);
                        if (confirm == JOptionPane.YES_OPTION){
                            try {
                                contactDAO.deleteContact(contactId); // ID Starts at 1
                                JOptionPane.showMessageDialog(Contactmanager.this, "Contact deleted successfully!");
                                refreshContactTable();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(Contactmanager.this, "Error deleting contact.", "Database Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });
        }

    private int getSelectedContactId(int selectedRow) {
        return (int) contactTable.getValueAt(selectedRow, 0);
    }
    
    private void refreshContactTable() {
            try {
                // Populate table with updated contact list
                contactTable.setModel(ContactTableModel.buildTable(contactDAO.getAllContact()));
                
                // hide ID
                contactTable.getColumnModel().getColumn(0).setMinWidth(0);
                contactTable.getColumnModel().getColumn(0).setMaxWidth(0);
                contactTable.getColumnModel().getColumn(0).setPreferredWidth(0);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Contactmanager gui = new Contactmanager();
            gui.setVisible(true);
            gui.refreshContactTable();
        });
    }
}
