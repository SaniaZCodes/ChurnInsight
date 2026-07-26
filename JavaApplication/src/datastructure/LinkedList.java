/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package datastructure;

import model.Customer;
import database.CustomerDAO;

public class LinkedList 
{
    private class Node 
    {
        Customer data;
        Node next;

        Node(Customer data) 
        {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;   
    private int size;    

    public LinkedList() 
    {
        head = null;
        size = 0;
    }

    // Add a new customer at the end of the list
    public void add(Customer customer) 
    {
        Node newNode = new Node(customer);
        if (head == null) 
        {
            head = newNode;
        } 
        else 
        {
            Node current = head;
            while (current.next != null) 
            {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Convert the linked list into a simple array (needed later for Merge Sort, BST building, etc.)
    public Customer[] toArray() 
    {
        Customer[] arr = new Customer[size];
        Node current = head;
        int i = 0;
        while (current != null) 
        {
            arr[i] = current.data;
            current = current.next;
            i++;
        }
        return arr;
    }

    public int getSize() 
    {
        return size;
    }
}