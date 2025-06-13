package packageName;

import org.postgresql.ds.PGSimpleDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Connection_to_db {
    private static Connection con;

    public static void startConnection() {
        try{
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setServerNames(new String[] {"localhost"});
            dataSource.setPortNumbers(new int[] {5432});
            dataSource.setDatabaseName("books");
            dataSource.setUser("postgres");
            dataSource.setPassword("12345");
            con = dataSource.getConnection();
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
