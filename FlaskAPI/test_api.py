import requests

data = {
    "gender": 1, "SeniorCitizen": 0, "Partner": 1, "Dependents": 0,
    "tenure": 12, "PhoneService": 1, "PaperlessBilling": 1,
    "MonthlyCharges": 70.5, "TotalCharges": 840.0,
    "MultipleLines_No phone service": 0, "MultipleLines_Yes": 0,
    "InternetService_Fiber optic": 1, "InternetService_No": 0,
    "OnlineSecurity_No internet service": 0, "OnlineSecurity_Yes": 0,
    "OnlineBackup_No internet service": 0, "OnlineBackup_Yes": 1,
    "DeviceProtection_No internet service": 0, "DeviceProtection_Yes": 0,
    "TechSupport_No internet service": 0, "TechSupport_Yes": 0,
    "StreamingTV_No internet service": 0, "StreamingTV_Yes": 1,
    "StreamingMovies_No internet service": 0, "StreamingMovies_Yes": 1,
    "Contract_One year": 0, "Contract_Two year": 0,
    "PaymentMethod_Credit card (automatic)": 0,
    "PaymentMethod_Electronic check": 1, "PaymentMethod_Mailed check": 0
}

response = requests.post("http://127.0.0.1:5000/predict", json=data)
print(response.json())