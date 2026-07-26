/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package datastructure;

import model.Customer;
import java.util.ArrayList;

public class Queue 
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

    private Node front; // to remove 
    private Node rear;  // to add 
    private int size;

    public Queue() 
    {
        front = null;
        rear = null;
        size = 0;
    }

    // Enqueue - add to the back of the line
    public void enqueue(Customer customer) 
    {
        Node newNode = new Node(customer);
        if (rear == null) 
        {
            front = newNode;
            rear = newNode;
        } 
        else 
        {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // Dequeue - remove from the front of the line
    public Customer dequeue() 
    {
        if (front == null) 
        {
            return null; // queue is empty
        }
        Customer data = front.data;
        front = front.next;
        if (front == null) 
        {
            rear = null; // queue is now empty
        }
        size--;
        return data;
    }

    // Peek - look at the front customer without removing them
    public Customer peek() 
    {
        if (front == null) 
        {
            return null;
        }
        else
        {
            return front.data;
        }        
    }

    public boolean isEmpty() 
    {
        return front == null;
    }

    public int getSize() 
    {
        return size;
    }

    // Helper: load a whole list (e.g. from GreedyAllocator) into the queue at once
    public void enqueueAll(ArrayList<Customer> customers) 
    {
        for (Customer c : customers) 
        {
            enqueue(c);
        }
    }
}