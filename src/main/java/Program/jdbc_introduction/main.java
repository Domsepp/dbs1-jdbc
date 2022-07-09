package Program.jdbc_introduction;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class main {
    private String server_;
    private String port_;
    private String dbName_;
    private String userName_;
    private String password_;
    private String databaseURL_;
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

    // function to sort hashmap by values
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    public void getActors(String keyword) throws SQLException{
        int size = 0;



        String searchKeyword = "'%"+keyword+"%'";
        databaseURL_ = "jdbc:postgresql://"+server_+":"+port_+"/"+dbName_;
        Connection connection = DriverManager.getConnection(databaseURL_,userName_,password_);
        Statement stmt = connection.createStatement();

        ResultSet res = stmt.executeQuery("SELECT name, movie_id FROM actor, movie WHERE movie_id = mid AND name LIKE "+searchKeyword);


        HashMap<String, List<String>> table = new HashMap<>();

        List<String> movies = new ArrayList<String>();
        String name;
        List<String> prevNames = new ArrayList<String>();

        while(res.next()){
            name = res.getString(1);
            if(prevNames.contains(name)) {
                movies = new ArrayList<>();
                for(int i = 0; i<table.get(name).size(); i++){
                    movies.add(table.get(name).get(i));
                }
                movies.add(res.getString(2));
                table.put(name,movies);
            }
            if(!prevNames.contains(name)) {
                String[] temp = new String[]{res.getString(2)};
                table.put(name, Arrays.asList(temp));
                prevNames.add(name);
            }
        }

        List<String> tempMovies = new ArrayList<String>();
        HashMap<String,Integer> otherActors = new HashMap<>();
        HashMap<String,Integer> sortedOtherActors = new HashMap<>();
        List<String> results = new ArrayList<String>();
        int count;
        ResultSet res3;
        ResultSet res4;

        String statement;
        PreparedStatement pStatement;

        for(Map.Entry<String,List<String>> entry : table.entrySet()){
            tempMovies = entry.getValue();
            System.out.println(entry.getKey()+":");
            for (String tempMovie : tempMovies) {
                statement = "SELECT name FROM actor, movie WHERE movie_id = mid AND movie_id =?";
                pStatement = connection.prepareStatement(statement);
                pStatement.setString(1,tempMovie);
                res3 = pStatement.executeQuery();
                results.clear();
                while (res3.next()) {
                    results.add(res3.getString(1));
                }
                for (String result : results) {
                    if (!result.contains("'")) {
                        statement = "SELECT COUNT(*) FROM actor a1, actor a2, movie m WHERE a1.movie_id = m.mid AND a2.movie_id = m.mid AND a1.name = ? AND a2.name = ?";
                        pStatement = connection.prepareStatement(statement);
                        pStatement.setString(1,result);
                        pStatement.setString(2,entry.getKey());
                        res4 = pStatement.executeQuery();
                        res4.next();
                        if(res4.getInt(1)!= 0) {
                            otherActors.put(result, res4.getInt(1));
                        }
                    }
                }
                sortedOtherActors = sortByValue(otherActors);
                System.out.println("\t"+tempMovie);
                int i = 5;
                for(Map.Entry<String, Integer> en : sortedOtherActors.entrySet()){
                    if(i<=0){
                        break;
                    }
                    System.out.println("\t\t"+en.getKey()+" ("+en.getValue()+")");
                    i--;
                }
                otherActors.clear();
                sortedOtherActors.clear();
            }
        }
    }

    public void connect(String name, String port, String userName, String password, String databaseURL){
        this.databaseURL_ = databaseURL;
        this.dbName_ = name;
        this.port_ = port;
        this.userName_ = userName;
        this.password_ = password;
    }
    public static void main(String[] args) throws SQLException {

        String name, port, userName, password, databaseURL, keyword;

        for(int i = 0; i<args.length; i++){
            if(args[i]=="-d"){
                name = args[i+1];
            }
            else if(args[i]=="-s"){
                databaseURL = args[i+1];
            }
            else if(args[i]=="-p"){
                port = args[i+1];
            }
            else if(args[i]=="-u"){
                userName = args[i+1];
            }
            else if(args[i]=="-pw"){
                password = args[i+1];
            }
        }

    }
}
