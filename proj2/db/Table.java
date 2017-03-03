package db;

import java.lang.reflect.Array;
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
        //System.out.println(colMap.keySet());
        for (String colName : colMap.keySet()) {
            table += colMap.get(colName).printColHead();
            if (countA < colMap.size() - 1) {
                table += ",";
                countA += 1;
            }
        }
        //System.out.println();
        table += "\n";

        String c = this.getColNames().get(0);
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
    public Table join(Table table2, Table roughTable) {
        if(isCartesianJoin(table2)) {

            String chosenT1Col1Name = this.getColNames().get(0);
            int length1 = this.getLinkedMap().get(chosenT1Col1Name).getValues().size();//# of rows in t1
            String chosenT2Col1Name = table2.getColNames().get(0);
            int length2 = table2.getLinkedMap().get(chosenT2Col1Name).getValues().size();//# of rows in t2
            //number or rows in 2nd table
            ArrayList<ArrayList<String>> colHeads = new ArrayList<ArrayList<String>>();
            for (String s1 : this.getLinkedMap().keySet()) {
                //x
                ArrayList<String> header = new ArrayList<>();
                String type = this.getLinkedMap().get(s1).getMyType();
                header.add(s1);
                header.add(type);
                colHeads.add(header); //add Column names of t1 to newColumnNames

            }
            for (String s2 : table2.getLinkedMap().keySet()) {
                ArrayList<String> header = new ArrayList<>();
                String type = table2.getLinkedMap().get(s2).getMyType();
                header.add(s2);
                header.add(type);
                colHeads.add(header); //add Column names of t2 to newColumnNames

            }
            for (ArrayList<String> a : colHeads) {
                //this.getLinkedMap().get(name).getMyType();
                String newColName = a.get(0);
                String newColType = a.get(1);
                Column newCol = new Column(newColName, newColType);
                roughTable.getColNames().add(newColName);
                roughTable.getLinkedMap().put(newColName, newCol); //create t3's linked map, but inserting in column names and
                //null for the values

            }
            for (int i = 0; i < length1; i++) {
                for (int j = 0; j < length2; j++) {
                    ArrayList<String> row = makeRow(this, table2, i, j);
                    roughTable.addRow(row);

                }


            }



        }
        /*
            for (String t1Key : t1.getLinkedMap().keySet()) {
                String t1Type = t2.getLinkedMap().get(t1Key).getMyType();
                //String t2Type = t2.getLinkedMap().get(t1Key).getMyType();
                if (t2.getLinkedMap().containsKey(t1Key) && (t1Type.equals(t2.getLinkedMap().get(t1Key).getMyType()))) {
                    Column a1 = t1.getLinkedMap().get(t1Key);
                    Column a2 = t2.getLinkedMap().get(t1Key);
                    String colType = t1.getLinkedMap().get(t1Key).getMyType();
                    Column t3sameCol = new Column(t1Key, colType); //is the key x-int? shouldn't it be just x?
                    for (String s : getCommonValues(a1, a2)) {
                        t3sameCol.addVal(s);

                    }
                    t3.getLinkedMap().put(t3sameCol.getName(), t3sameCol);

                } else {
                    t3.getLinkedMap().put(t1Key, t1.getLinkedMap().get(t1Key));
                }
                */
        //let's hope it returns the new table changed/joined
        return roughTable;

    }



    private boolean isCartesianJoin(Table t2) {
        //no columns in common
        for(String t1Key : this.getLinkedMap().keySet()) {
            if (t2.getLinkedMap().keySet().contains(t1Key)) {
                return false;
            }
        }
        return true;

    }
    public void addRow(ArrayList<String> rowToAdd) {
        int count = 0;
        for(Column c : this.getLinkedMap().values()) {
            String valueToAdd = rowToAdd.get(count);
            c.addVal(valueToAdd);
            count += 1;
        }


    }

    public static ArrayList<String> makeRow (Table t1, Table t2, int rowNumT1, int rowNumT2) {
        //returns a full row, at a certaing index between 2 tables
        ArrayList<String> newRow = new ArrayList<>();

        for(Column c : t1.getLinkedMap().values()) {
            String item = c.getValues().get(rowNumT1);
            newRow.add(item);
        }
        for(Column c : t2.getLinkedMap().values()) {
            String item = c.getValues().get(rowNumT2);
            newRow.add(item);
        }
        return newRow; //does this return null?
    }
    private static boolean equalColVals (Column c1, Column c2, int index) {
        if (!((c1.getMyType()).equals(c2.getMyType()))) {
            return false;
        }
        else {
            return c1.getValues().get(index).equals(c2.getValues().get(index));
        }


    }
    private static ArrayList<String> getCommonValues(Column c1, Column c2) {
        ArrayList<String> commonValues = new ArrayList<>();
        for(int i = 0; i < c1.myValues.size(); i ++) {
            if (equalColVals(c1, c2, i)) {
                commonValues.add(c1.getValues().get(i));
            }
        }

        return commonValues;
    }



    //merge method to merge rows?

    private static void doInnerJoin(Table t2) {
        //use recursive nature

    }


}
