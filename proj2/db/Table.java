package db;

import java.util.*;

/**
 * Created by jhinukbarman on 2/26/17.
 */

public class Table {
    private ArrayList<String> colNames;
    public String tableName;
    private LinkedHashMap<String, Column> colMap;
    // A Map of column name and its data type


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (tableName != null ? !tableName.equals(table.tableName) : table.tableName != null) return false;
        if (colMap != null ? !colMap.equals(table.colMap) : table.colMap != null) return false;
        return colNames != null ? colNames.equals(table.colNames) : table.colNames == null;
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (colMap != null ? colMap.hashCode() : 0);
        result = 31 * result + (colNames != null ? colNames.hashCode() : 0);
        return result;
    }
    // private Map<String, String> colTypes;

    public Table(String name){
        tableName = name;
        colMap = new LinkedHashMap<String, Column>();
        colNames = new ArrayList<String>();
    }


    public void addRow(List<String> literalList){
        int count = 0;
        for (String key : colMap.keySet()) {
            colMap.get(key).addVal(literalList.get(count));
            count += 1;
        }

    }

    public String getTableName(){
        return tableName;
    }

    public String printTable(){
        int countA = 0;
        String table = "";
        for (String colName : colMap.keySet()) {
            table += colMap.get(colName).printColHead();
            if (countA < colMap.size() - 1) {
                table += ",";
                countA += 1;
            }
        }
        //System.out.println();
        table += "\n";

        String c = getColNames().get(0);
        int length = colMap.get(c).getValues().size();
        for (int i = 0; i < length; i++) {
            int countB = 0;
            for (String colValue: colMap.keySet()) {
                table += colMap.get(colValue).printColVal(i);
                if (countB < colMap.size() - 1) {
                    table += ",";
                    countB += 1;
                }
            }
            table += "\n";
        }
        return table;
    }

    public LinkedHashMap<String, Column> getLinkedMap(){
        return colMap;
    }

    public ArrayList<String> getColNames(){
        return colNames;
    }

    //TA tip: class is "noun"...join is NOT a noun so it shouldn't be a class
    //use recursion? later: change parameter to take in any arraylist of tables
    // (to join multiple tables)
    public Table join(Table t2) {
        Table t3 = new Table("t3");
        if (ifCartesianJoin(t2)) {
            doCartesianJoin(t2);
        } else {
            doInnerJoin(t2);
        }


        return t3;
    }

    private boolean ifCartesianJoin(Table t2) {
        for (String t1Key : this.getLinkedMap().keySet()) {
            //String t1Type = t2.getLinkedMap().get(t1Key).getMyType();
            //dont need to compare column types
            if (t2.getLinkedMap().containsKey(t1Key)) {
                return false;
            }
        }
        return true;

        //return (t2.getLinkedMap().containsKey(t1Key) && (t1Type.equals(t2.getLinkedMap().get(t1Key).getMyType())))


    }

    private static void doCartesianJoin(Table t2) {

    }

    //merge method to merge rows?

    private static void doInnerJoin(Table t2) {
        //use recursive nature

    }


}
