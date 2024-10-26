/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  LENOVO
 * Created: Oct 26, 2024
 */

CREATE TABLE contacts (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email   VARCHAR(100),
    address VARCHAR(100)
);