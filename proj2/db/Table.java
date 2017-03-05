package db;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public String getName() {
        return tableName;
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
            ArrayList<ArrayList<String>> colHeads = new ArrayList<>();

            for (String s1 : this.getLinkedMap().keySet()) {
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
                String newColName = a.get(0);
                String newColType = a.get(1);
                Column newCol = new Column(newColName, newColType);

                roughTable.getLinkedMap().put(newColName, newCol);
                roughTable.getColNames().add(newColName);
                //create t3's linked map, but inserting in column names and
                //null for the values

            }
            for (int i = 0; i < length1; i++) {
                for (int j = 0; j < length2; j++) {
                   // System.out.println(this.printTable());
                   // System.out.println(table2.printTable());
                  //  System.out.println(roughTable.printTable());
                    ArrayList<String> row = makeCartesianRow(this, table2, i, j);
                    roughTable.addRow(row);
                }
            }
        }

        else {

            LinkedHashMap<Integer, ArrayList<String>> pairLinkedMap = new LinkedHashMap<>();
            int mapIndex = 0;
            int t1ColNum = this.getLinkedMap().keySet().size();
            int t2ColNum = table2.getLinkedMap().keySet().size();
            for (Column c1: this.getLinkedMap().values()) {
                for (Column c2 : table2.getLinkedMap().values()) {
                    if (c1.getName().equals(c2.getName())) {
                        //create an arraylist row
                        roughTable.getColNames().add(c1.getName());
                        String newColName = c1.getName();
                        String newColType = c1.getMyType();
                        Column newCol = new Column(newColName, newColType);
                        roughTable.getLinkedMap().put(newColName, newCol);// may delete, well be adding colNames to
                        //rough table later
                        for (int i = 0; i < c1.getValues().size(); i++) {
                            for (int j = 0; j < c2.getValues().size(); j++) {
                                if (canJoin(this, table2, i, j)) {

                                    ArrayList<String> row = makeInnerRow(c1.getName(), this, table2, i, j);

                                    pairLinkedMap.put(mapIndex, row);
                                    mapIndex += 1;
                                }
                            }
                        }
                    }
                }
            }

            //merged rows created in pariLInked Map
            ArrayList<ArrayList<String>> columnNamesList = colOrderList(this, table2); //ERROR


            //gets the columns of new order in correct order ----

            for(ArrayList<String> arrList : columnNamesList) {
                Column newCol = new Column(arrList.get(0), arrList.get(1));

                if(!(roughTable.getLinkedMap().keySet().contains(arrList.get(0)))){
                    roughTable.getColNames().add(arrList.get(0));
                    roughTable.getLinkedMap().put(arrList.get(0), newCol);
                }
                //put columnName and put column in rough table




            }
            int ind = 0;
            for (Column col : roughTable.getLinkedMap().values()) { //fill in columns of rough table
                for (ArrayList<String> list : pairLinkedMap.values()) { //add values to the columns, add 0th element
                    //of the pair linked Map list to all the columns of  rough table,
                    //then add 1st element of the pair linked Map list to all the
                    col.getValues().add(list.get(ind));

                }
                ind += 1;

            }
        }

        //System.out.print("inner join: " + roughTable.printTable());
        return roughTable;

    }

    public static boolean canJoin(Table t1, Table t2, int t1RowNum, int t2RowNum) {
        for (Column c1 : t1.getLinkedMap().values()) {
            if (t2.getLinkedMap().keySet().contains(c1.getName())) {
                Column c2 = t2.getLinkedMap().get(c1.getName());
                if (!(c1.getValues().get(t1RowNum)).equals(c2.getValues().get(t2RowNum))) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<ArrayList<String>> colOrderList(Table t1, Table t2) {
        ArrayList<ArrayList<String>> colOrder = new ArrayList<>();
        for (Column c1 : t1.getLinkedMap().values()) { // add the names of all the shared columns in list
            if (t2.getLinkedMap().keySet().contains(c1.getName())) {
                ArrayList<String> newArrList = new ArrayList<>();
                newArrList.add(c1.getName());
                newArrList.add(c1.getMyType());
                colOrder.add(newArrList);
            }
        }

        for (Column c1 : t1.getLinkedMap().values()) { //add the names of the unique columns in table 1, starting from
            // the left most column
            ArrayList<ArrayList<String>> temp1 = new ArrayList<>(colOrder);
            for (ArrayList<String> a : temp1) {
                if (!(a.get(0).equals(c1.getName()))) {
                    ArrayList<String> newArrList = new ArrayList<>();
                    newArrList.add(c1.getName());
                    newArrList.add(c1.getMyType());
                    colOrder.add(newArrList);

                }
            }
        }

        ArrayList<ArrayList<String>> temp2 = new ArrayList<>(colOrder);
        ArrayList<String> cNames = new ArrayList<>();
        for (ArrayList<String> a : temp2) {
            cNames.add(a.get(0));
        }
        for (Column c2 : t2.getLinkedMap().values()) { //add unames of the unique columns in table 2, starting from
            //the left most column
            if (!(containsItem(colNames, c2.getName()))) {
                    ArrayList<String> newArrList = new ArrayList<>();
                    newArrList.add(c2.getName());
                    newArrList.add(c2.getMyType());
                    colOrder.add(newArrList);
                    colNames.add(c2.getName());

            }
        }

        return colOrder;
    }

    public static boolean containsItem(ArrayList<String> arr, String s) {
        for (String str : arr) {
            if (str.equals(s)) {
                return true;
            }
        }
        return false;
    }



    public static ArrayList<String> makeInnerRow(String startColName, Table t1, Table t2, int t1Index, int t2Index) {
        ArrayList<String> newEditedRow = new ArrayList<>();

        for (Column c1 : t1.getLinkedMap().values()) {
            String item = c1.getValues().get(t1Index);
            newEditedRow.add(item);


        }

        for (Column c2 : t2.getLinkedMap().values()) {
            if (!(c2.getName().equals(startColName))) {
                String item = c2.getValues().get(t2Index);
                newEditedRow.add(item);
            }
        }
        return  newEditedRow;

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

    public static ArrayList<String> makeCartesianRow (Table t1, Table t2, int rowNumT1, int rowNumT2) {
        //returns a full row, at a certaing index between 2 tables
        ArrayList<String> newRow = new ArrayList<>();

        for(Column c : t1.getLinkedMap().values()) {
            //System.out.println(c.getValues());
            String item = c.getValues().get(rowNumT1); //ERROR at dis line
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






    //----------------------------------------------------//




    public String insertRow(String vals) {
        //vals = 1,2,3,4,5,6
        int tokenIndex = 0;
        String token;
        StringTokenizer st = new StringTokenizer(vals, ",");
        if ((st.countTokens() != this.getColNames().size())) {
            return "ERROR: invalid value inputs";
        }
        while (st.hasMoreTokens()) {
            String type = "";
            token = st.nextToken();
            String currColName = this.getColNames().get(tokenIndex);
            String currColType = this.getLinkedMap().get(currColName).getMyType();

            //check type. check if wrong type?
            if (token.startsWith("\'")) {
                type = "string";
            } else if (token.contains(".")) {
                type = "float";
                //float f = Float.parseFloat(token);
                //token = (BigDecimal.valueOf(f).setScale(3, RoundingMode.DOWN).toString());
            } else if (token.equals("NOVALUE")) {
                type = currColType;
                token = "NOVALUE";
            } else {
                type = "int";
            }
            //if the colName type matches type of the token (literal)
            if (type.equals(currColType)) {
                this.getLinkedMap().get(currColName).addVal(token);
             } else {
                return "ERROR: Value type does not match column type.";
            }
            tokenIndex += 1;
        }
        return "";
    }


}
