/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{

    private static final String URL = "jdbc:mysql://127.0.0.1:3307/churn_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() 
    {
        Connection conn = null;
        try 
        {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } 
        catch (SQLException e) 
        {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) 
    {
        getConnection();
    }
}