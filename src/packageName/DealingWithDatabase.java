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
//            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        return con;
    }
    public static void closeConnection() {
        try{
            con.close();
        }catch(SQLException e) {
            System.out.println("Closing Connection failed");
            e.printStackTrace();
        }
    }

}
