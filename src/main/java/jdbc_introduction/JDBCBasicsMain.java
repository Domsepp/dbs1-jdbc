package jdbc_introduction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCBasicsMain {

    public static void main(String[] args) throws SQLException {
        JDBCIntroduction jdbcIntroduction = new JDBCIntroduction();
        jdbcIntroduction.execute();

    }
}
