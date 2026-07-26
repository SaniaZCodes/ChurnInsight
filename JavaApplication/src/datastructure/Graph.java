/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package datastructure;

import model.Customer;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Arrays;


public class Graph 
{
    // adjacency list: customerId -> list of connected customerIds
    private Map<Integer, List<Integer>> adjacencyList;
    
    // quick lookup: customerId -> Customer object
    private Map<Integer, Customer> customerMap;

    public Graph() 
    {
        adjacencyList = new HashMap<>();
        customerMap = new HashMap<>();
    }

    /* Build the graph - connects customers who share Contract Type AND Internet Service
     (and have somewhat similar monthly charges)*/
    public void buildGraph(Customer[] customers) 
    {
        // First, register every customer as a node
        for (Customer c : customers) 
        {
            customerMap.put(c.getCustomerId(), c);
            adjacencyList.put(c.getCustomerId(), new ArrayList<>());
        }

        // Now compare every pair of customers and connect similar ones
        for (int i = 0; i < customers.length; i++) 
        {
            for (int j = i + 1; j < customers.length; j++) 
            {
                Customer a = customers[i];
                Customer b = customers[j];

                if (isSimilar(a, b)) 
                {
                    adjacencyList.get(a.getCustomerId()).add(b.getCustomerId());
                    adjacencyList.get(b.getCustomerId()).add(a.getCustomerId());
                }
            }
        }
    }

    // Two customers are "similar" if same contract type, same internet service,
    // and monthly charges within Rs. 300 of each other
    private boolean isSimilar(Customer a, Customer b) 
    {
        boolean sameContract = a.getContractType().equalsIgnoreCase(b.getContractType());
        boolean sameInternet = a.getInternetService().equalsIgnoreCase(b.getInternetService());
        boolean similarCharges = Math.abs(a.getMonthlyCharges() - b.getMonthlyCharges()) <= 300;

        return sameContract && sameInternet && similarCharges;
    }

    // BFS - find all customers similar to the given one, up to maxDepth "hops"
    public List<Customer> findSimilarCustomers(int startCustomerId, int maxDepth) 
    {
        List<Customer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        java.util.Queue<Integer> queue = new java.util.LinkedList<>();
        Map<Integer, Integer> depthMap = new HashMap<>();

        queue.add(startCustomerId);
        visited.add(startCustomerId);
        depthMap.put(startCustomerId, 0);

        while (!queue.isEmpty()) 
        {
            int currentId = queue.poll();
            int currentDepth = depthMap.get(currentId);

            if (currentDepth > 0) 
            { // don't include the customer themselves
                result.add(customerMap.get(currentId));
            }

            if (currentDepth < maxDepth) 
            {
                for (int neighborId : adjacencyList.get(currentId)) 
                {
                    if (!visited.contains(neighborId)) 
                    {
                        visited.add(neighborId);
                        depthMap.put(neighborId, currentDepth + 1);
                        queue.add(neighborId);
                    }
                }
            }
        }

        return result;
    }
}