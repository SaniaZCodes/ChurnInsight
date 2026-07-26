/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package datastructure;

import model.Customer;
import database.CustomerDAO;

public class BST 
{
    private class Node 
    {
        Customer data;
        Node left;
        Node right;

        Node(Customer data) 
        {
            this.data = data;
            left = null;
            right = null;
        }
    }

    private Node root;

    public BST() 
    {
        root = null;
    }

    // Insert one customer into the tree (based on customerId)
    public void insert(Customer customer) 
    {
        root = insertRec(root, customer);
    }

    private Node insertRec(Node node, Customer customer) 
    {
        if (node == null) 
        {
            return new Node(customer);
        }
        if (customer.getCustomerId() < node.data.getCustomerId()) 
        {
            node.left = insertRec(node.left, customer);
        } 
        else if (customer.getCustomerId() > node.data.getCustomerId()) 
        {
            node.right = insertRec(node.right, customer);
        }
        return node;
    }

    // Build the whole tree from an array of customers (used after loading from Linked List)
    public void buildFromArray(Customer[] customers) 
    {
        for (Customer c : customers) 
        {
            insert(c);
        }
    }

    // Search for a customer by ID
    public Customer search(int customerId) 
    {
        return searchRec(root, customerId);
    }

    private Customer searchRec(Node node, int customerId) 
    {
        if (node == null) 
        {
            return null; // not found
        }
        if (customerId == node.data.getCustomerId()) 
        {
            return node.data;
        } 
        else if (customerId < node.data.getCustomerId()) 
        {
            return searchRec(node.left, customerId);
        } 
        else 
        {
            return searchRec(node.right, customerId);
        }
    }
}