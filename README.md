# Job Shop Accounting System

This project includes both a Java console application and a web application for managing a job shop accounting system. It interacts with an Azure SQL Database to perform various operations related to customers, departments, processes, assemblies, accounts, jobs, and transactions.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Azure SQL Database instance
- JDBC driver for SQL Server
- Apache Tomcat 9 or another Java servlet container

## Project Structure
```
job-shop-accounting
│
├── src/
│   ├── java/
│   │   └── jobshop/
│   │       ├── JobShopDatabaseApp.java
│   │       └── DataHandler1.java
│   │
│   └── webapp/
│       ├── index.jsp
│       ├── insertCustomerForm.jsp
│       ├── insertCustomer.jsp
│       ├── retrieveCustomersForm.jsp
│       └── retrieveCustomers.jsp
│
├── sql/
│   ├── create_tables.sql
│   └── create_procedures.sql
│
│
├── ER Diagram
└── Project
├── README.md
└── LICENSE
```
## Setup

1. Clone the repository to your local machine.

2. Set up the SQL tables and stored procedures:
   - Connect to your Azure SQL Database using a SQL client tool (e.g., SQL Server Management Studio, Azure Data Studio).
   - Execute the `sql/create_tables.sql` script to create the necessary tables in your database.
   - Execute the `sql/create_procedures.sql` script to create the required stored procedures in your database.

3. Update the database connection details:
   - Open `src/java/jobshop/JobShopDatabaseApp.java` and `src/java/jobshop/DataHandler1.java`.
   - In both files, replace the placeholders for server name, database name, username, and password with your Azure SQL Database connection details:
     - `<server-name>`: Your Azure SQL Database server name
     - `<database-name>`: Your database name
     - `<username>`: Your database username
     - `<password>`: Your database password

4. Compile the Java files:
   - Open a terminal or command prompt.
   - Navigate to the `src` directory.
   - Run the following command:
     ```
     javac java/jobshop/*.java
     ```

5. Set up your Java servlet container (e.g., Apache Tomcat) and deploy the web application.

## Running the Console Application

1. Open a terminal or command prompt.
2. Navigate to the `src` directory.
3. Run the application:
    ```
    java jobshop.JobShopDatabaseApp
    ```
4. Follow the on-screen prompts to interact with the job shop accounting system.

## Using the Web Application

1. Deploy the web application to your servlet container.
2. Access the application through a web browser.
3. Use the web interface to insert new customers and retrieve customer information.

The web application includes the following JSP files:
- `insertCustomerForm.jsp`: Form to insert a new customer.
- `insertCustomer.jsp`: Processes the customer insertion and displays the result.
- `retrieveCustomersForm.jsp`: Form to retrieve customers by category range.
- `retrieveCustomers.jsp`: Displays the retrieved customers.

## Dependencies

- Azure SQL Database instance
- JDBC driver for SQL Server (mssql-jdbc-11.2.3.jre8.jar or later)
- Java servlet container (e.g., Apache Tomcat 9)

Make sure to include the JDBC driver in your project's classpath when compiling and running the application.

## Troubleshooting

- If you encounter connection issues, ensure your Azure SQL Database firewall settings allow connections from your IP address.
- For web application deployment issues, check your servlet container's log files for detailed error messages.


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Thanks to Dr. Le Gruenwald for the project requirements and guidance.
- Azure SQL Database documentation for connection string formats and best practices.