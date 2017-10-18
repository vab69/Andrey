package SQL_JAVA;

/**
 * Created by vab on 01.10.2017.
 */
import java.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connect {
    private java.sql.Connection con = null;
    private final String url = "jdbc:sqlserver://";
    private final String serverName = "";
    private final String portNumber = "1433";
    private final String databaseName = "SampleDB";
    private final String userName = "";
    private final String password = "";
    // Сообщает драйверу о необходимости использовать сервером побочного курсора,
    // что позволяет использовать несколько активных выражений
    // для подключения.
    private final String selectMethod = "cursor";

    public Connection getCon() {
        return con;
    }

    // Конструктор
    public Connect() {
    }

    private String getConnectionUrl() {
        return url + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";selectMethod=" + selectMethod + ";";
    }

    private java.sql.Connection getConnection() {
        try {
            Class.forName( "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
            con = java.sql.DriverManager.getConnection( getConnectionUrl(), userName, password );
            if (con != null) System.out.println( "Connection Successful!" );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println( "Error Trace in getConnection() : " + e.getMessage() );
        }
        return con;
    }

     /*
          Вывести свойства драйвера, сведения о базе данных
     */

    public void displayDbProperties() {
        java.sql.DatabaseMetaData dm = null;
        java.sql.ResultSet rs = null;
        try {
            con = this.getConnection();
            if (con != null) {
                dm = con.getMetaData();
                System.out.println( "Driver Information" );
                System.out.println( "\tDriver Name: " + dm.getDriverName() );
                System.out.println( "\tDriver Version: " + dm.getDriverVersion() );
                System.out.println( "\nDatabase Information " );
                System.out.println( "\tDatabase Name: " + dm.getDatabaseProductName() );
                System.out.println( "\tDatabase Version: " + dm.getDatabaseProductVersion() );
                System.out.println( "Avalilable Catalogs " );
                rs = dm.getCatalogs();
                while (rs.next()) {
                    System.out.println( "\tcatalog: " + rs.getString( 1 ) );
                }
                rs.close();
                rs = null;
                closeConnection();
            } else System.out.println( "Error: No active Connection" );
        } catch (Exception e) {
            e.printStackTrace();
        }
        dm = null;
    }

    private void closeConnection() {
        try {
            if (con != null)
                con.close();
            con = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Connect myDbTest = new Connect();
    //    myDbTest.displayDbProperties();
        executeStatement(myDbTest.getConnection());

    }
    public static void executeStatement(Connection con) {
        try {
            String SQL = "SELECT * FROM SampleDB.dbo.CustCopy ";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                System.out.println(rs.getString("cust_name") + ", " + rs.getString("cust_address")+ "," +

                       rs.getString("cust_contact"));
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}