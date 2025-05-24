package packageName;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DealingWithDatabase {
    private static Connection con;

    public static void startConnection() {
        String url = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=Books;"
                + "user=sa;"
                + "password=12345;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";
        try{
            con = DriverManager.getConnection(url);
        }catch(SQLException e) {
            System.out.println("Start Connection failed");
            System.out.println(e.getMessage());
        }
    }
    public static Connection getConnection() throws SQLException {
        if(con != null && con.isValid(2)) {
            return con;
        }
        else{
            startConnection();
            return con;
        }
    }
    public static void closeConnection() {
        try{
            if(con != null && !con.isClosed()){
                con.close();
            }
        }catch(SQLException e) {
            System.out.println("Closing Connection failed");
        }
    }

}
