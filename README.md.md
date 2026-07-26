# &#x20;**ChurnInsight**



&#x20;*AI-Powered Customer Churn Prediction \& Retention System*



ChurnInsight is a customer churn prediction and retention management system developed using Java, MySQL, Python, Flask, and Machine Learning. The system identifies customers who are likely to leave a telecom company and helps management take proactive retention actions.



\---



### &#x20;Technologies Used



##### &#x20;Frontend

\- Java Swing



##### &#x20;Backend

\- MySQL

\- JDBC



##### &#x20;Machine Learning

\- Python

\- Flask API

\- Logistic Regression



\---



##### &#x20;Data Structures \& Algorithms



\- Linked List

\- Binary Search Tree (BST)

\- Queue

\- Stack

\- Graph

\- Breadth First Search (BFS)

\- Merge Sort

\- Greedy Algorithm

\- Priority Queue

\- HashMap

\- Set



\---



### &#x20;Core Features and Data Structures Used



&#x20;**1. Load Customer Records**

Data Structure: Linked List

Customer records retrieved from MySQL are stored in a custom Linked List for dynamic storage and processing.



&#x20;**2. Search Customer**

Data Structure: Binary Search Tree (BST)

A BST is built using customer records to perform efficient customer searches based on Customer ID.



&#x20;**3. High-Risk Customer Analysis**

Algorithm: Merge Sort

Customers are sorted in descending order of churn probability so that the highest-risk customers appear first.





&#x20;**4. Retention Budget Allocation**

Algorithm: Greedy Algorithm

The system allocates retention offers by selecting the highest-risk customers while staying within the available budget.





&#x20;**5. Customer Outreach Management**

Data Structure: Queue (FIFO)

Selected customers are placed into a queue and processed in the order they were selected.





&#x20;**6. Undo Last Action**

Data Structure: Stack (LIFO)

Offer actions are stored in a stack, allowing the most recent action to be undone first.





&#x20;**7. Similar Customer Analysis**

Data Structure: Graph

Each customer is represented as a node, while relationships between similar customers are represented as edges.





&#x20;**8. Similar Customer Discovery**

Algorithm: Breadth First Search (BFS)

BFS traverses the customer graph level by level to identify customers with similar characteristics.



&#x20;**9. Urgent Customer Processing**

Data Structure: Priority Queue

Customers with the highest churn probability are processed first based on priority.



&#x20;**10. Fast Customer Lookup**

Data Structure: HashMap

HashMaps are used for efficient customer retrieval and graph adjacency management.





### Machine Learning Workflow



Customer Data

→ Flask API

→ Logistic Regression Model

→ Churn Probability

→ Java Application



The Machine Learning model predicts the probability that a customer will churn based on customer attributes such as:



\- Contract Type

\- Tenure

\- Internet Service

\- Monthly Charges

\- Payment Method

\- Online Services





### Database Operations



The system implements complete CRUD functionality:



\-> **Create**

Add New Customer



\-> **Read**

View All Customers



\-> **Update**

Update Existing Customer Information



\-> **Delete**

Delete Customer Records



Database access is handled through the DAO (Data Access Object) pattern using JDBC and PreparedStatement.





#### Project Architecture



MySQL Database

↓

CustomerDAO

↓

ArrayList

↓

LinkedList

↓

Customer Array

↓

Merge Sort

↓

BST

↓

Application Features



Additional Processing:



\- Queue → Customer Outreach

\- Stack → Undo Operations

\- Graph + BFS → Similar Customers

\- Greedy Algorithm → Budget Allocation

\- Priority Queue → Urgent Customer Handling

\- Logistic Regression → Churn Prediction



\---



#### Project Structure



&#x20;**JavaApplication:**

\- GUI Screens

\- Database Operations

\- Data Structures

\- Business Logic



&#x20;**FlaskAPI:**

Contains:

\- Flask Server

\- Logistic Regression Model

\- Prediction API

\- Trained Model Files



**Dataset:**

Contains:

\- Original Dataset

\- Database Dataset

\- Prediction Dataset



