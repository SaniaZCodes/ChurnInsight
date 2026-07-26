/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package datastructure;

import model.Customer;
import java.util.ArrayList;

public class GreedyAllocator 
{
    // Simple discount cost formula: 10% of monthly charges
    private static double calculateDiscountCost(Customer c) 
    {
        return c.getMonthlyCharges() * 0.10;
    }

    // Takes an already-sorted array (highest risk first) and a budget,
    // returns the list of customers selected to receive an offer
    public static ArrayList<Customer> allocate(Customer[] sortedCustomers, double budget) 
    {
        ArrayList<Customer> selected = new ArrayList<>();
        double remainingBudget = budget;

        for (Customer c : sortedCustomers) 
        {
            // Skip customers who already got an offer before
            if ("Offered".equalsIgnoreCase(c.getOfferStatus())) 
            {
                continue;
            }

            double cost = calculateDiscountCost(c);

            if (cost <= remainingBudget) 
            {
                selected.add(c);
                remainingBudget -= cost;
            }
            // If it doesn't fit, we just skip this one and try the next (greedy behavior)
        }

        return selected;
    }
}