package db;

import java.util.*;

/**
 * Created by jhinukbarman on 2/26/17.
 */

public class Table {
    public String tableName;
    private ArrayList<String> colNames;
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
    }


    public void addRow(List<String> literalList){

    }
    public void addRow(String rowString) {

        StringTokenizer st = new StringTokenizer(rowString, ",");
        int tokenIndex = 0;
        String next;
        String currentColumn;
        //List<T> columnList;

        while (st.hasMoreTokens()) {
            next = st.nextToken();
            currentColumn = colNames.get(tokenIndex);
            if (colMap.containsKey(currentColumn)) {
                //columnList = colMap.get(currentColumn);
                //columnList.add((T) next);
                //colMap.put(currentColumn, (ArrayList<T>) columnList);
            }
            else {
               // columnList = new ArrayList<T>();
               // columnList.add((T) next );
               // colMap.put(colNames.get(tokenIndex), (ArrayList<T>) columnList);
            }

            tokenIndex++;
        }
    }

    public String getTableName(){
        return tableName;
    }

    public void printTable(){
        int countA = 0;
        for (String colName : colMap.keySet()) {
            colMap.get(colName).printColHead();
            if (countA < colMap.size() - 1) {
                System.out.print(",");
                countA += 1;
            }
        }
        System.out.println();

        for (int i = 0; i < colMap.size(); i++) {
            int countB = 0;
            for (String colValue: colMap.keySet()) {
                colMap.get(colValue).printColVal(i);
                if (countB < colMap.size() - 1) {
                    System.out.print(",");
                    countB += 1;
                }
            }
            System.out.println();
        }
    }

    public LinkedHashMap<String, Column> getLinkedMap(){
        return colMap;
    }

    public ArrayList<String> getColNames(){
        return colNames;
    }
}
