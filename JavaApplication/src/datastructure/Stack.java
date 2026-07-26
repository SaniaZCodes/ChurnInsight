/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package datastructure;

public class Stack 
{
    public class Action 
    {
        public int customerId;
        public String previousStatus;
        public String newStatus;

        public Action(int customerId, String previousStatus, String newStatus) 
        {
            this.customerId = customerId;
            this.previousStatus = previousStatus;
            this.newStatus = newStatus;
        }

        @Override
        public String toString() 
        {
            return "Customer " + customerId + ": " + previousStatus + " -> " + newStatus;
        }
    }

    private class Node 
    {
        Action data;
        Node next;

        Node(Action data)
        {
            this.data = data;
            this.next = null;
        }
    }

    private Node top; 
    private int size;

    public Stack() 
    {
        top = null;
        size = 0;
    }

    // Push - add a new action on top
    public void push(Action action)
    {
        Node newNode = new Node(action);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // Pop - remove and return the most recent action
    public Action pop()
    {
        if (top == null) 
        {
            return null;
        }
        Action data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public boolean isEmpty()
    {
        return top == null;
    }

    public int getSize()
    {
        return size;
    }
}