Finora – Personal Finance Tracker 💰

Track • Plan • Grow

Finora is a Java-based desktop application designed to simplify personal financial management. It helps users track income and expenses, manage budgets, monitor savings goals, and review financial records through a user-friendly graphical interface.

📌 Project Overview

Managing personal finances can be challenging when income, expenses, and savings records are maintained separately. Finora provides a centralized application to organize financial transactions, monitor spending, and support better financial decisions.

The application is built using Java Swing for the graphical user interface and MySQL for database management. JDBC connects the Java application to the database.

🎯 Objectives

Track income and expenses in one place.

Manage monthly budgets and spending limits.

Categorize transactions for easier analysis.

Monitor savings goals and progress.

Maintain transaction history.

Generate useful financial summaries.

Protect personal financial information through user authentication.

✨ Features

User Registration and Login – Manage personal accounts.

Income Management – Record and maintain income transactions.

Expense Management – Add, edit, delete, and view expenses.

Budget Management – Set and monitor budget limits.

Savings Goals – Define financial targets and track progress.

Transaction History – Review previous financial records.

Reports and Analysis – Understand income and spending patterns.

Profile Management – Maintain user information and account settings.

Only retain features that are implemented in the current version of the project.

🛠️ Technologies Used

Technologies used:
Java
Application logic and object-oriented programming
Java Swing
Desktop graphical user interface
MySQL
Relational database
JDBC
Database connectivity
MySQL Workbench
Database design and management
Visual Studio Code
Development environment
Git and GitHub
Version control and collaboration

🏗️ Application Architecture

Finora follows a desktop application architecture:
Presentation Layer: Java Swing components display forms, navigation, dashboards, and transaction records.
Application Logic Layer: Java classes validate user input and implement finance-related operations.
Database Layer: JDBC executes SQL queries to communicate with MySQL.
Storage Layer: MySQL stores user details, income, expenses, budgets, categories, and savings goals.

⚙️ Prerequisites

Before running the application, install:
Java Development Kit (JDK)
MySQL Server
MySQL Connector/J JDBC driver
Visual Studio Code or another Java-compatible IDE
HOW TO RUN THE PROJECT?

Step 1: Download the Project

Clone the repository:

git clone YOUR_GITHUB_REPOSITORY_URL

Alternatively, download the project as a ZIP file from GitHub and extract it.

Open the project folder in Visual Studio Code.

Step 2: Configure MySQL Credentials

Ensure MySQL Server is installed and running on your computer.

Open the database connection configuration in the project and enter your local MySQL username and password.

For example:

String username = "root";
String password = "YOUR_MYSQL_PASSWORD";

Replace YOUR_MYSQL_PASSWORD with your own MySQL password.

Step 3: Run the Application

Open the project in Visual Studio Code.

Ensure the JDK and MySQL Connector/J driver are configured.

Execute these commands in the terminal:
For compilation use: javac -cp ".;lib\mysql-connector-j-26.10.0.jar" *.java  
For running the application use:java -cp ".;lib\mysql-connector-j-26.10.0.jar" FinoraHero

Finora will open its desktop interface, allowing you to register and log in.

Troubleshooting
Access denied: Check your MySQL username and password.

Unknown database: Run the provided SQL import script.

JDBC driver not found: Check the MySQL Connector/J configuration.

Connection failed: Ensure MySQL Server is running and the connection URL is correct.
