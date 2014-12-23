package workUtils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestSQLServer {

    public static void main(String[] args) {
        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://172.16.50.28:1433;databaseName=token";
        String userName = "sa";
        String userPwd = "i-Sprint2011";
        Connection dbConn;

        try {

            Class.forName(driverName);

            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);

            System.out.println("Connection Successful!");

            Statement statement = dbConn.createStatement();
            statement
                    .executeUpdate("CREATE TABLE token (id int IDENTITY(1,1) primary key,  userid VARCHAR(225) ,  email VARCHAR(225) ,mobile VARCHAR(225) ,uuid VARCHAR(225) ,  code VARCHAR(225) , balance money, seeddata IMAGE ,  createtime int ,  downloadtime int ,  column1 VARCHAR(225) ,  column2 VARCHAR(225) ,  column3 VARCHAR(225) ,  column4 VARCHAR(225) ,  column5 VARCHAR(225) ,  column6 VARCHAR(225))");

        } catch(Exception e) {

            e.printStackTrace();

        }

    }
}
