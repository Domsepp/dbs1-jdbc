package jdbc_introduction;

import java.sql.*;

public class JDBCIntroduction {
    public void execute() throws SQLException {
        String server = "localhost";
        int port = 5432;
        String dbName = "imdb";
        //url structure for postgres:
        String databaseURL = "jdbc:postgresql://" + server + ":" + port + "/" + dbName;
        String userName = "dbs1_psql";
        String password = "admin";
        Connection connection = DriverManager.getConnection(databaseURL, userName, password);
        //the connection can be used to get info about the database:
        System.out.println(connection.getMetaData().getDatabaseProductName());
        System.out.println(connection.getMetaData().getDatabaseMajorVersion());
        //the connection can be used to issue queries against the database
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT DISTINCT name,role FROM actor");
        //result set is an iterator over the results of the query
        System.out.println(resultSet.next()); //next returns true if the pointer could be advanced to the next tuple, false otherwise
        //getString / getInt / getTimestamp / ... are methods that work on the tuple, that the pointer is currently pointing at
        //These methods expect either an attribute name or the attribute index (Starting at 1! - Why? Nobody knows...)
        System.out.println(resultSet.getString(1));
        System.out.println(resultSet.getString("name"));
        //prepared statements can include variables that can be filled programmatically:
        //this is VERY helpful in defending against the nightmare of every dba: SQL Injections!
        PreparedStatement stmt2 = connection.prepareStatement("SELECT DISTINCT name,role FROM actor WHERE name = ?");
        stmt2.setString(1,"Longval, Jean-Marc");
        resultSet = stmt2.executeQuery();
        resultSet.next();
        System.out.println(resultSet.getString("name"));
    }
}
