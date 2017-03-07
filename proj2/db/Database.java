package db;

//import java.io.IOException;
import java.util.HashMap;

public class Database {
    private HashMap<String, Table> h;
    //Table myTable;

    public Database() {
        h = new HashMap<String, Table>();
    }

    public String transact(String query) {
       // Parse.
        Parser p = new Parser(this);
        return p.eval(query);
        //return "should return empty string";
        //return "from dataBase class hi";
    }

    public HashMap<String, Table> getMap() {
        return h;
    }


    public void addTable(String tableName, Table table) {
        h.put(tableName, table);
    }
}
