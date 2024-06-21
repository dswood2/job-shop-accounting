package jobshop;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;
public class DataHandler1 {
    private Connection conn;
    // Azure SQL connection credentials
    private String server = "<server-name>.database.windows.net";
    private String database = "<database-name>";
    private String username = "<username>";
    private String password = "<password>";
    // Resulting connection string
    final private String url =
            String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
                    server, database, username, password);
    // Initialize and save the database connection
    private void getDBConnection() throws SQLException {
        if (conn != null) {
            return;
        }
        this.conn = DriverManager.getConnection(url);
    }
    // Insert a new customer using Query 1 stored procedure
    public boolean insertCustomer(String customerName, String customerAddress, int category) throws SQLException {
        getDBConnection();
        final String sqlQuery = "{call InsertCustomer(?,?,?)}";
        final PreparedStatement stmt = conn.prepareCall(sqlQuery);
        stmt.setString(1, customerName);
        stmt.setString(2, customerAddress);
        stmt.setInt(3, category);
        return stmt.executeUpdate() == 1;
    }
    // Retrieve customer names by category range using Query 12 stored procedure
    public List<String> retrieveCustomerNamesByCategoryRange(int minCategory, int maxCategory) throws SQLException {
        getDBConnection();
        List<String> customerNames = new ArrayList<>();
        final String sqlQuery = "{call RetrieveCustomerNamesByCategoryRange(?,?)}";
        final PreparedStatement stmt = conn.prepareCall(sqlQuery);
        stmt.setInt(1, minCategory);
        stmt.setInt(2, maxCategory);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String customerName = rs.getString("customer_name");
            customerNames.add(customerName);
        }
        return customerNames;
    }
}