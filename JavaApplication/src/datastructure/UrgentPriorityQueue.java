/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package datastructure;

import model.Customer;

public class UrgentPriorityQueue 
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

    private Node front;
    private int size;

    public UrgentPriorityQueue()
    {
        front = null;
        size = 0;
    }

    public void insert(Customer customer) 
    {
        Node newNode = new Node(customer);

        if (front == null || customer.getChurnProbability() > front.data.getChurnProbability()) 
        {
            newNode.next = front;
            front = newNode;
            size++;
            return;
        }

        Node current = front;
        while (current.next != null && current.next.data.getChurnProbability() >= customer.getChurnProbability()) 
        {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    public Customer removeMostUrgent() 
    {
        if (front == null) 
        {
            return null;
        }
        Customer data = front.data;
        front = front.next;
        size--;
        return data;
    }

    public Customer peekMostUrgent()
    {
        if (front == null) 
        {
            return null;
        }
        return front.data;
    }

    public boolean isEmpty()
    {
        return front == null;
    }

    public int getSize()
    {
        return size;
    }
}