from flask import Flask, request, jsonify
import joblib
import pandas as pd

app = Flask(__name__)

model = joblib.load('churn_model.pkl')
scaler = joblib.load('scaler.pkl')
model_columns = joblib.load('model_columns.pkl')

cols_to_scale = ['tenure', 'MonthlyCharges', 'TotalCharges']

# Simple manual encoding maps (matching LabelEncoder's alphabetical behavior)
binary_maps = {
    'gender': {'Female': 0, 'Male': 1},
    'Partner': {'No': 0, 'Yes': 1},
    'Dependents': {'No': 0, 'Yes': 1},
    'PhoneService': {'No': 0, 'Yes': 1},
    'PaperlessBilling': {'No': 0, 'Yes': 1}
}

multi_cols = ['MultipleLines', 'InternetService', 'OnlineSecurity', 'OnlineBackup',
              'DeviceProtection', 'TechSupport', 'StreamingTV', 'StreamingMovies',
              'Contract', 'PaymentMethod']

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    input_df = pd.DataFrame([data])

    # Step 1: Encode binary columns
    for col, mapping in binary_maps.items():
        input_df[col] = input_df[col].map(mapping)

    # Step 2: One-hot encode multi-category columns (same as training)
    input_df = pd.get_dummies(input_df, columns=multi_cols)

    # Step 3: Align columns to match training data exactly
    input_df = input_df.reindex(columns=model_columns, fill_value=0)

    # Step 4: Scale numeric columns
    input_df[cols_to_scale] = scaler.transform(input_df[cols_to_scale])

    # Step 5: Predict
    probability = model.predict_proba(input_df)[:, 1][0]

    return jsonify({
        'churn_probability': float(probability),
        'churn_prediction': 'Yes' if probability > 0.5 else 'No'
    })

if __name__ == '__main__':
    app.run(debug=True, port=5000)