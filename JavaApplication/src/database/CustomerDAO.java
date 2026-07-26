/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package database;

import model.Customer;
import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO 
{
    // Fetch ALL customers from database, return as a simple list
    public ArrayList<Customer> getAllCustomers() 
    {
        ArrayList<Customer> list = new ArrayList<>();
        String query = "SELECT * FROM customers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) 
        {

            while (rs.next()) 
            {
                Customer c = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("phone_number"),
                    rs.getString("gender"),
                    rs.getInt("senior_citizen"),
                    rs.getString("partner"),
                    rs.getString("dependents"),
                    rs.getInt("tenure"),
                    rs.getString("phone_service"),
                    rs.getString("multiple_lines"),
                    rs.getString("internet_service"),
                    rs.getString("online_security"),
                    rs.getString("online_backup"),
                    rs.getString("device_protection"),
                    rs.getString("tech_support"),
                    rs.getString("streaming_tv"),
                    rs.getString("streaming_movies"),
                    rs.getString("contract_type"),
                    rs.getString("paperless_billing"),
                    rs.getString("payment_method"),
                    rs.getDouble("monthly_charges"),
                    rs.getDouble("total_charges"),
                    rs.getString("churn_actual"),
                    rs.getDouble("churn_probability"),
                    rs.getString("offer_status")
                );
                list.add(c);
            }

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return list;
    }

    // Update a customer's offer_status (used later by Queue processing)
    public void updateOfferStatus(int customerId, String status) 
    {
        String query = "UPDATE customers SET offer_status = ? WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) 
        {
            ps.setString(1, status);
            ps.setInt(2, customerId);
            ps.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    
    // Delete Customer
    public void deleteCustomer(int customerId)
    {
        String query = "DELETE FROM customers WHERE customer_id = ?";

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setInt(1, customerId);
            ps.executeUpdate();

            System.out.println("Customer deleted successfully!");

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }


    // Update Customer
    public void updateCustomer(
        int customerId,
        String gender,
        int tenure,
        String contractType,
        String internetService,
        double monthlyCharges)
    {
        String query =
                "UPDATE customers SET " +
                "gender = ?, " +
                "tenure = ?, " +
                "contract_type = ?, " +
                "internet_service = ?, " +
                "monthly_charges = ? " +
                "WHERE customer_id = ?";

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1, gender);
            ps.setInt(2, tenure);
            ps.setString(3, contractType);
            ps.setString(4, internetService);
            ps.setDouble(5, monthlyCharges);
            ps.setInt(6, customerId);

            ps.executeUpdate();

            System.out.println("Customer updated successfully!");

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Get the next available customer ID (max id + 1)
    public int getNextCustomerId() 
    {
        String query = "SELECT MAX(customer_id) FROM customers";
        try 
        {
            Connection conn = DBConnection.getConnection();
            if (conn == null) 
            {
                System.out.println("Warning: Database connection is null, returning default ID 1");
                return 1;
            }
            
            try (PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return rs.getInt(1) + 1;
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return 1;
    }

    // Add New Customer - full fields version
    public void insertCustomer(
        String phoneNumber,
        String gender,
        int seniorCitizen,
        String partner,
        String dependents,
        int tenure,
        String phoneService,
        String multipleLines,
        String internetService,
        String onlineSecurity,
        String onlineBackup,
        String deviceProtection,
        String techSupport,
        String streamingTV,
        String streamingMovies,
        String contractType,
        String paperlessBilling,
        String paymentMethod,
        double monthlyCharges,
        String offerStatus)
    {
        String query =
            "INSERT INTO customers (" +
            "phone_number, gender, senior_citizen, partner, dependents, " +
            "tenure, phone_service, multiple_lines, internet_service, " +
            "online_security, online_backup, device_protection, tech_support, " +
            "streaming_tv, streaming_movies, contract_type, paperless_billing, " +
            "payment_method, monthly_charges, total_charges, churn_actual, " +
            "churn_probability, offer_status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1, phoneNumber);
            ps.setString(2, gender);
            ps.setInt(3, seniorCitizen);
            ps.setString(4, partner);
            ps.setString(5, dependents);
            ps.setInt(6, tenure);
            ps.setString(7, phoneService);
            ps.setString(8, multipleLines);
            ps.setString(9, internetService);
            ps.setString(10, onlineSecurity);
            ps.setString(11, onlineBackup);
            ps.setString(12, deviceProtection);
            ps.setString(13, techSupport);
            ps.setString(14, streamingTV);
            ps.setString(15, streamingMovies);
            ps.setString(16, contractType);
            ps.setString(17, paperlessBilling);
            ps.setString(18, paymentMethod);
            ps.setDouble(19, monthlyCharges);
            ps.setDouble(20, monthlyCharges * tenure); // total_charges = monthly * tenure
            ps.setString(21, "No"); // churn_actual
            ps.setDouble(22, 0.00); // churn_probability
            ps.setString(23, offerStatus);

            ps.executeUpdate();
            System.out.println("Customer added successfully!");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Quick test
    public static void main(String[] args) 
    {
        CustomerDAO dao = new CustomerDAO();
        ArrayList<Customer> customers = dao.getAllCustomers();
        System.out.println("Total customers loaded: " + customers.size());
        if (!customers.isEmpty()) 
        {
            System.out.println("First customer: " + customers.get(0));
        }
    }
}