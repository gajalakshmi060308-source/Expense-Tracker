
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/finora_db";
    private static final String USER = "root";

    // Replace this with your actual MySQL password
    private static final String PASSWORD = "Gopik#246";

    public static Connection getConnection() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create the database if it does not exist
            String databaseURL = "jdbc:mysql://localhost:3306/";
            try (Connection setupCon = DriverManager.getConnection(
                    databaseURL, USER, PASSWORD);
                 Statement stmt = setupCon.createStatement()) {

                stmt.executeUpdate(
                        "CREATE DATABASE IF NOT EXISTS finora_db");
            }

            // Connect to the database
            con = DriverManager.getConnection(
                    URL, USER, PASSWORD);

            // Create the users table if it does not exist
            try (Statement stmt = con.createStatement()) {
                String createTable = """
                        CREATE TABLE IF NOT EXISTS users (
                            user_id INT PRIMARY KEY AUTO_INCREMENT,
                            full_name VARCHAR(100) NOT NULL,
                            email VARCHAR(150) NOT NULL UNIQUE,
                            phone VARCHAR(10) NOT NULL,
                            password_hash VARCHAR(255) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                        """;

                stmt.executeUpdate(createTable);
                String createIncomeTable = """
    CREATE TABLE IF NOT EXISTS incomes (
        income_id INT PRIMARY KEY AUTO_INCREMENT,
        user_id INT NOT NULL,
        source VARCHAR(60) NOT NULL,
        amount DECIMAL(12,2) NOT NULL,
        income_date DATE NOT NULL,
        description VARCHAR(500),
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES users(user_id)
    )
    """;

stmt.executeUpdate(createIncomeTable);

System.out.println("Income table is ready!");

String createExpenseTable="""
CREATE TABLE IF NOT EXISTS expenses (
    expense_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category VARCHAR(60) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    expense_date DATE NOT NULL,
    payment_method VARCHAR(30),
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
""";
stmt.executeUpdate(createExpenseTable);
String createBudgetTable="""
CREATE TABLE IF NOT EXISTS budgets (
    budget_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    budget_month DATE NOT NULL,
    category VARCHAR(60) NOT NULL,
    budget_amount DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_month_category
        (user_id, budget_month, category),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
""";
stmt.executeUpdate(createBudgetTable);
String createCategoryTable="""
CREATE TABLE IF NOT EXISTS expense_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_category (user_id, category_name),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);""";
stmt.executeUpdate(createCategoryTable);

            }

            System.out.println(
                    "Database and tables are ready!");
            System.out.println(
                    "Database connected successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }

        return con;
    }
}
