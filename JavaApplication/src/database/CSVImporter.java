/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CSVImporter 
{
    public static void main(String[] args) 
    {       
        String csvFile = "customers_for_database.csv";
        String line;
        String splitBy = ",";
        boolean firstLine = true;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             Connection conn = DBConnection.getConnection()) 
        {
            String insertSQL = "INSERT INTO customers (gender, senior_citizen, partner, dependents, tenure, "
                    + "phone_service, multiple_lines, internet_service, online_security, online_backup, "
                    + "device_protection, tech_support, streaming_tv, streaming_movies, contract_type, "
                    + "paperless_billing, payment_method, monthly_charges, total_charges, churn_actual, churn_probability) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(insertSQL);
            int count = 0;

            while ((line = br.readLine()) != null) 
            {
                if (firstLine) 
                {
                    firstLine = false; // skip header row
                    continue;
                }

                String[] data = line.split(splitBy);

                ps.setString(1, data[0]);   // gender
                ps.setInt(2, Integer.parseInt(data[1]));   // senior_citizen
                ps.setString(3, data[2]);   // partner
                ps.setString(4, data[3]);   // dependents
                ps.setInt(5, Integer.parseInt(data[4]));   // tenure
                ps.setString(6, data[5]);   // phone_service
                ps.setString(7, data[6]);   // multiple_lines
                ps.setString(8, data[7]);   // internet_service
                ps.setString(9, data[8]);   // online_security
                ps.setString(10, data[9]);  // online_backup
                ps.setString(11, data[10]); // device_protection
                ps.setString(12, data[11]); // tech_support
                ps.setString(13, data[12]); // streaming_tv
                ps.setString(14, data[13]); // streaming_movies
                ps.setString(15, data[14]); // contract_type
                ps.setString(16, data[15]); // paperless_billing
                ps.setString(17, data[16]); // payment_method
                ps.setDouble(18, Double.parseDouble(data[17])); // monthly_charges
                ps.setDouble(19, Double.parseDouble(data[18])); // total_charges
                ps.setString(20, data[19]); // churn_actual (Churn column: 0 or 1)
                ps.setDouble(21, Double.parseDouble(data[20])); // churn_probability

                ps.executeUpdate();
                count++;
            }

            System.out.println("Successfully imported " + count + " rows!");

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}