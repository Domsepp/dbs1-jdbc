package Program.jdbc_introduction;

import java.sql.SQLException;

public class main {
    public static void main(String[] args) throws SQLException {
        DBConnection DBConnection = new DBConnection();
        DBConnection.execute();
        System.out.println("--------------------------------------------------");
        DBConnection.getMovies("the");
    }
}
