/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

public class Customer 
{
    private int customerId;
    private String phoneNumber;
    private String gender;
    private int seniorCitizen;
    private String partner;
    private String dependents;
    private int tenure;
    private String phoneService;
    private String multipleLines;
    private String internetService;
    private String onlineSecurity;
    private String onlineBackup;
    private String deviceProtection;
    private String techSupport;
    private String streamingTv;
    private String streamingMovies;
    private String contractType;
    private String paperlessBilling;
    private String paymentMethod;
    private double monthlyCharges;
    private double totalCharges;
    private String churnActual;
    private double churnProbability;
    private String offerStatus;

    
    public Customer(int customerId, String phoneNumber, String gender, int seniorCitizen, 
                    String partner,String dependents, int tenure, 
                    String phoneService, String multipleLines,
                    String internetService, String onlineSecurity, 
                    String onlineBackup,String deviceProtection, 
                    String techSupport, String streamingTv,
                    String streamingMovies, String contractType, 
                    String paperlessBilling,String paymentMethod, 
                    double monthlyCharges, double totalCharges,
                    String churnActual, double churnProbability, 
                    String offerStatus) 
    {
        this.customerId = customerId;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.seniorCitizen = seniorCitizen;
        this.partner = partner;
        this.dependents = dependents;
        this.tenure = tenure;
        this.phoneService = phoneService;
        this.multipleLines = multipleLines;
        this.internetService = internetService;
        this.onlineSecurity = onlineSecurity;
        this.onlineBackup = onlineBackup;
        this.deviceProtection = deviceProtection;
        this.techSupport = techSupport;
        this.streamingTv = streamingTv;
        this.streamingMovies = streamingMovies;
        this.contractType = contractType;
        this.paperlessBilling = paperlessBilling;
        this.paymentMethod = paymentMethod;
        this.monthlyCharges = monthlyCharges;
        this.totalCharges = totalCharges;
        this.churnActual = churnActual;
        this.churnProbability = churnProbability;
        this.offerStatus = offerStatus;
    }

    
    public int getCustomerId() 
    { 
        return customerId; 
    }
    
    public String getPhoneNumber() 
    { 
        return phoneNumber; 
    }
    
    public String getGender() 
    { 
        return gender; 
    }
    
    public int getSeniorCitizen() 
    { 
        return seniorCitizen; 
    }
    
    public String getPartner() 
    { 
        return partner; 
    }
    
    public String getDependents() 
    { 
        return dependents; 
    }
    
    public int getTenure() 
    { 
        return tenure; 
    }
    
    public String getPhoneService() 
    { 
        return phoneService; 
    }
    
    public String getMultipleLines() 
    { 
        return multipleLines; 
    }
    
    public String getInternetService() 
    { 
        return internetService; 
    }
    
    public String getOnlineSecurity() 
    { 
        return onlineSecurity; 
    }
    
    public String getOnlineBackup() 
    { 
        return onlineBackup; 
    }
    
    public String getDeviceProtection() 
    { 
        return deviceProtection; 
    }
    
    public String getTechSupport() 
    { 
        return techSupport; 
    }
    
    public String getStreamingTv() 
    { 
        return streamingTv; 
    }
    
    public String getStreamingMovies() 
    { 
        return streamingMovies; 
    }
    
    public String getContractType() 
    { 
        return contractType; 
    }
    
    public String getPaperlessBilling() 
    { 
        return paperlessBilling; 
    }
    
    public String getPaymentMethod() 
    { 
        return paymentMethod; 
    }
    
    public double getMonthlyCharges() 
    { 
        return monthlyCharges;
    }
    
    public double getTotalCharges() 
    {
        return totalCharges;
    }
    
    public String getChurnActual() 
    { 
        return churnActual; 
    }
    
    public double getChurnProbability() 
    { 
        return churnProbability;
    }
    
    public String getOfferStatus() 
    { 
        return offerStatus;
    }

    // Setters (needed later for updates, e.g. offerStatus after processing queue)
    public void setChurnProbability(double churnProbability) 
    { 
        this.churnProbability = churnProbability; 
    }
    
    public void setOfferStatus(String offerStatus) 
    { 
        this.offerStatus = offerStatus; 
    }

    @Override
    public String toString() 
    {
        return "ID: " + customerId + " | Gender: " + gender 
                + " | Tenure: " + tenure + " | Contract: " + contractType 
                + " | Risk: " + String.format("%.2f", churnProbability)
                + " | Status: " + offerStatus;
    }
}