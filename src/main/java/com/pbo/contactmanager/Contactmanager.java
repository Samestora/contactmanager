/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.pbo.contactmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
        
        // Style
        // No on hover
        addButton.setBorderPainted(false);
        updateButton.setBorderPainted(false);
        deleteButton.setBorderPainted(false);
        
        // Colorful        
        addButton.setBackground(Color.GREEN);
        deleteButton.setBackground(Color.ORANGE);
        updateButton.setBackground(Color.CYAN);

        // Get resources and paste
        addButton.setIcon(resizeImage("/add.png", 25, 25));
        deleteButton.setIcon(resizeImage("/delete.png", 25, 25));
        updateButton.setIcon(resizeImage("/update.png", 25, 25));
        
         // Nested Layout
        JPanel panel = new JPanel(new GridLayout(6, 1)); // Main panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3)); // Button panel

        // Adding labels and fields to the main panel
        panel.add(createChildPanel("Name:", nameField));
        panel.add(createChildPanel("Phone:", phoneField));
        panel.add(createChildPanel("Email:", emailField));
        panel.add(createChildPanel("Address:", addressField));

        // Adding buttons to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel);
        
        // Setting up the table to display contacts
        contactTable = new JTable();
        JScrollPane tablePane = new JScrollPane(contactTable);
        
        // Set input pane and tablepane position based on layout
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
    

    private ImageIcon resizeImage(String imagePath, int width, int height) {
        try {
            // Load the image
            Image originalImage = ImageIO.read(this.getClass().getResource(imagePath));
            // Resize the image
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Or return a default icon
        }
    }
    
    private JPanel createChildPanel(String label, JTextField textField) {
        JPanel childPanel = new JPanel(new GridLayout(1, 2));
        childPanel.add(new JLabel(label, SwingConstants.CENTER));
        childPanel.add(textField);
        return childPanel;
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
