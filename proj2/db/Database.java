package db;

import java.io.IOException;
import java.util.HashMap;

public class Database {
    public HashMap<String, Table> h;
    //Table myTable;

    public Database(){
        h = new HashMap<String, Table>();
    }
/*
    public Database(Table t) {
        // YOUR CODE HERE
        h = new HashMap<String, Table>();
        myTable = t;
        h.put(myTable.getTableName(), myTable);
       // ArrayList<>;
    }
 */

    public String transact(String query) {
       // Parse.
        Parser p = new Parser(this);
        return p.eval(query);
        //return "should return empty string";
        //return "from dataBase class hi";
    }

    public HashMap<String, Table> getMap(){
        return h;
    }


    public void addTable(String tableName, Table table) {
        h.put(tableName, table);
    }
}
