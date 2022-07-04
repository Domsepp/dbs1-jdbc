package Program.jdbc_introduction;

import java.sql.*;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

public class DBConnection {
    private String server_;
    private String port_;
    private String dbName_;
    private String userName_;
    private String password_;
    private String databaseURL_;
    public DBConnection(String server, String port, String dbName, String userName, String password){
        this.server_ = server;
        this.port_ = port;
        this.dbName_ = dbName;
        this.userName_ = userName;
        this.password_ = password;
    }
    public DBConnection(){
        this.server_ = "localhost";
        this.port_ = "5432";
        this.dbName_ = "imdb";
        this.userName_ = "postgres";
        this.password_ = "panter";
    }

    public void getMovies(String keyword) throws SQLException{
        String searchKeyword = "'%"+keyword+"%'";
        databaseURL_ = "jdbc:postgresql://"+server_+":"+port_+"/"+dbName_;
        Connection connection = DriverManager.getConnection(databaseURL_,userName_,password_);
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery("SELECT title, year, genre g, a.name, ac.name FROM movie m, genre g, actor a, actress ac WHERE m.mid = g.movie_id AND m.mid = a.movie_id AND m.mid = ac.movie_id AND title LIKE "+searchKeyword);

        String prevTitle = "";
        String prevActress = "";
        String prevActor = "";
        String prevGenre = "";
        List<String> genres = new ArrayList<String>();
        List<String> actors = new ArrayList<String>();

        boolean cont = true;
        res.next();
        while(true){
            prevTitle = res.getString(1);
            prevActress = res.getString(5);
            prevActor = res.getString(4);


            if(!res.next()) break;
            if(res.getString(1).equals(prevTitle)){
                if(!res.getString(3).equals(prevGenre) && !genres.contains(res.getString(3))){
                    genres.add(res.getString(3));
                }
                if(!res.getString(4).equals(prevActor) && actors.size()<6){
                    actors.add(res.getString(4));
                }
                if(!res.getString(5).equals(prevActress) && actors.size()<6){
                    actors.add(res.getString(5));
                }
                continue;
            }
            else {
                actors = actors.stream().sorted().collect(Collectors.toList());
                System.out.println("Titel: " + res.getString(1) + "; Jahr: " + res.getString(2) + "; Genre: " + genres + "; Actors: "+actors);
                genres.clear();
                actors.clear();
            }
            genres.add(res.getString(3));
        }
    }

    public void execute() throws SQLException {
        String server = "localhost";
        int port = 5432;
        String dbName = "imdb";
        //url structure for postgres:
        String databaseURL = "jdbc:postgresql://" + server + ":" + port + "/" + dbName;
        String userName = "postgres";
        String password = "panter";
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
