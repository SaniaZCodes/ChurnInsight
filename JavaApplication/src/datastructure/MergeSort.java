/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package datastructure;

import model.Customer;
import database.CustomerDAO;

public class MergeSort 
{

    // Public method to start the sort - sorts descending by churnProbability (highest risk first)
    public static void sort(Customer[] arr) 
    {
        if (arr.length > 1) 
        {
            mergeSort(arr, 0, arr.length - 1);
        }
    }

    private static void mergeSort(Customer[] arr, int left, int right) 
    {
        if (left < right) 
        {
            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);       // sort left half
            mergeSort(arr, mid + 1, right);  // sort right half
            merge(arr, left, mid, right);    // merge them back together
        }
    }

    // joins two sorted halfs
    
    private static void merge(Customer[] arr, int left, int mid, int right) 
    {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Customer[] leftArr = new Customer[n1];
        Customer[] rightArr = new Customer[n2];

        for (int i = 0; i < n1; i++) 
        {
            leftArr[i] = arr[left + i];
        }
        
        for (int j = 0; j < n2; j++) 
        {
            rightArr[j] = arr[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2)
        {
            // Descending order: higher churnProbability comes first
            if (leftArr[i].getChurnProbability() >= rightArr[j].getChurnProbability())
            {
                arr[k] = leftArr[i];
                i++;
            } 
            else 
            {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) 
        {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        while (j < n2) 
        {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }
}