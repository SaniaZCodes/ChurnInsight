/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ml;

import model.Customer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FlaskConnector 
{

    private static final String FLASK_URL = "http://127.0.0.1:5000/predict";

    public static double getChurnProbability(Customer c) 
    {
        try 
        {
            String json = "{"
                + "\"gender\": \"" + c.getGender() + "\","
                + "\"SeniorCitizen\": " + c.getSeniorCitizen() + ","
                + "\"Partner\": \"" + c.getPartner() + "\","
                + "\"Dependents\": \"" + c.getDependents() + "\","
                + "\"tenure\": " + c.getTenure() + ","
                + "\"PhoneService\": \"" + c.getPhoneService() + "\","
                + "\"MultipleLines\": \"" + c.getMultipleLines() + "\","
                + "\"InternetService\": \"" + c.getInternetService() + "\","
                + "\"OnlineSecurity\": \"" + c.getOnlineSecurity() + "\","
                + "\"OnlineBackup\": \"" + c.getOnlineBackup() + "\","
                + "\"DeviceProtection\": \"" + c.getDeviceProtection() + "\","
                + "\"TechSupport\": \"" + c.getTechSupport() + "\","
                + "\"StreamingTV\": \"" + c.getStreamingTv() + "\","
                + "\"StreamingMovies\": \"" + c.getStreamingMovies() + "\","
                + "\"Contract\": \"" + c.getContractType() + "\","
                + "\"PaperlessBilling\": \"" + c.getPaperlessBilling() + "\","
                + "\"PaymentMethod\": \"" + c.getPaymentMethod() + "\","
                + "\"MonthlyCharges\": " + c.getMonthlyCharges() + ","
                + "\"TotalCharges\": " + c.getTotalCharges()
                + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FLASK_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            String key = "\"churn_probability\":";
            int start = body.indexOf(key) + key.length();
            int end = body.indexOf(",", start);
            if (end == -1) end = body.indexOf("}", start);
            String value = body.substring(start, end).trim();

            return Double.parseDouble(value);

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) 
    {
        database.CustomerDAO dao = new database.CustomerDAO();
        datastructure.LinkedList list = new datastructure.LinkedList();

        for (Customer c : dao.getAllCustomers()) 
        {
            list.add(c);
        }

        Customer testCustomer = list.toArray()[0];
        System.out.println("Testing with: " + testCustomer);

        double probability = getChurnProbability(testCustomer);
        System.out.println("Churn probability from Flask: " + probability);
    }
}