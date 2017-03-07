package db;

//import java.awt.image.AreaAveragingScaleFilter;
//import java.lang.reflect.Array;
//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.*;

/**
 * Created by Jhinuk Barman on 2/26/2017.
 */
public class Table {
    private String name;
    public LinkedHashMap<String, Column> colMap;
    public ArrayList<String> colNames;
    public Table(String n) {
        name = n;
        colMap = new LinkedHashMap<String, Column>();
        colNames = new ArrayList<String>();
    }

    public void changeName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Table table = (Table) o;

        if (name != null ? !name.equals(table.name) : table.name != null) {
            return false;
        }
        return colMap != null ? colMap.equals(table.colMap) : table.colMap == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (colMap != null ? colMap.hashCode() : 0);
        return result;
    }

    public void addRow(List<String> literalList) {
        int count = 0;
        for (String key : colMap.keySet()) {
            colMap.get(key).addVal(literalList.get(count));
            count += 1;
        }
    }

    public String getName() {
        return name;
    }

    public boolean mapEmpty() {
        for (String key : colMap.keySet()) {
            if (colMap.get(key) != null) {
                return false;
            }
        }
        return true;

    }

    public String printTable() {
        //print out the column heads
        //String table = "";
        int countA = 0;
        String fullTable = "";
        for (String colName : colMap.keySet()) {
            fullTable += colMap.get(colName).printColHead();
            if (countA < colMap.size() - 1) {
                fullTable += ",";
                countA += 1;

            }
        }
        fullTable += "\n";
        //prints out the contents of the columns

        if (mapEmpty()) {
            return "";
        }

        String c = getColNames().get(0);

        int length = colMap.get(c).getValues().size();
        for (int i = 0; i < length; i++) {
            int countB = 0;
            for (String colValue : colMap.keySet()) {
                fullTable += colMap.get(colValue).printColVal(i);
                if (countB < colMap.size() - 1) {
                    fullTable += ",";
                    countB += 1;
                }
            }
            fullTable += "\n";
        }
        return fullTable; // is this returning null?

    }

    public Table join(Table table2, Table roughTable) {
        if (isCartesianJoin(table2)) {

            String chosenT1Col1Name = this.getColNames().get(0);
            int length1 = this.getLinkedMap().get(chosenT1Col1Name).getValues().size(); //# of rows in t1
            String chosenT2Col1Name = table2.getColNames().get(0);
            int length2 = table2.getLinkedMap().get(chosenT2Col1Name).getValues().size(); //# of rows in t2
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
                    ArrayList<String> row = makeCartesianRow(this, table2, i, j);
                    roughTable.addRow(row);

                }


            }

        } else {

            LinkedHashMap<Integer, ArrayList<String>> pairLinkedMap = new LinkedHashMap<>();
            int mapIndex = 0;
            ArrayList<String> sameCols = getSameColumnNames(this, table2);
            for (Column c1: this.getLinkedMap().values()) {
                for (Column c2 : table2.getLinkedMap().values()) {
                    if (c1.getName().equals(c2.getName())) {
                        //create an arraylist row
                        roughTable.getColNames().add(c1.getName());
                        String newColName = c1.getName();
                        String newColType = c1.getMyType();
                        Column newCol = new Column(newColName, newColType);
                        roughTable.getLinkedMap().put(newColName, newCol); // may delete, well be adding colNames to
                        //rough table later
                        for (int i = 0; i < c1.getValues().size(); i++) {
                            for (int j = 0; j < c2.getValues().size(); j++) {

                                if (sameCols.size() > 1) {

                                    //if doing join(with tables that have multiple similar columns) then do see if row at
                                    //t1rowIndex in table 1 and row at t2rowIndex in table 2 if similar column pairs exist
                                    //in both tables; for ex,row 0 of t1 can only join with row 0 of t2 if, the z and w
                                    //values in row 0 of t1 correspond with z and w values of row 0 of t2.
                                    if (canJoinMultiple(this, table2, i, j, sameCols)) {
                                        /*if (mapIndex >= sameCols.size()) {
                                            break;
                                        }*/
                                        ArrayList<String> row = makeMultRow(this, table2, i, j, sameCols);
                                        pairLinkedMap.put(mapIndex, row);
                                        mapIndex += 1;

                                    }


                                } else if (canJoin(this, table2, i, j)) {

                                    //if testing to see if you could join for multiple columns, you have to check if
                                    //if all pairs of zw, are the ssatisfied

                                    ArrayList<String> row = makeInnerRow(c1.getName(), this, table2, i, j);

                                    pairLinkedMap.put(mapIndex, row);
                                    mapIndex += 1;
                                }
                            }
                        }
                        //t1ColNum -= 1;
                        //t2ColNum -= 1;


                    }
                }
            }
                   /* else if ((this.getLinkedMap().keySet().size() - t1ColNum) >= 0) {
                        roughTable.getColNames().add(c1.getName());
                        String newColName = c1.getName();
                        String newColType = c1.getMyType();
                        Column newCol = new Column(newColName, newColType);
                        roughTable.getLinkedMap().put(newColName, newCol);
                        t1ColNum -= 1;

                    }
                    else if ((table2.getLinkedMap().keySet().size() - t2ColNum) >= 0) {
                        roughTable.getColNames().add(c2.getName());
                        String newColName = c2.getName();
                        String newColType = c2.getMyType();
                        Column newCol = new Column(newColName, newColType);
                        roughTable.getLinkedMap().put(newColName, newCol);
                        t2ColNum -= 1;
                    }



                }


            }*/
            //merged rows created in pariLInked Map
            ArrayList<ArrayList<String>> columnNamesList = colOrderList(this, table2);
            /*for(ArrayList<String> x : columnNamesList) {
                for (String s : x) {
                    System.out.print(s + "   ");

                }

            }*/


            //gets the columns of new order in correct order ----

            for (ArrayList<String> arrList : columnNamesList) {
                Column newCol = new Column(arrList.get(0), arrList.get(1));

                if (!(roughTable.getLinkedMap().keySet().contains(arrList.get(0)))) {
                    roughTable.getColNames().add(arrList.get(0));

                    roughTable.getLinkedMap().put(arrList.get(0), newCol);
                }
                //put columnName and put column in rough table

            }

            if (sameCols.size() > 1) { //remove repeated rows from pairLinkedMap

                /*if (pairLinkedMap.get(0) == null) {
                    Table emptyTable = new Table("t3");
                    return emptyTable;
                }*/
                ArrayList<Integer> indecesToRemove = new ArrayList<>();
                for (int i = 0; i < pairLinkedMap.keySet().size(); i++) {
                    for (int q = i + 1; q < pairLinkedMap.keySet().size(); q++) {
                        if (pairLinkedMap.get(i).equals(pairLinkedMap.get(q))) {
                            indecesToRemove.add(q);
                        }

                    }
                }

                for (int index : indecesToRemove) {
                    pairLinkedMap.remove(index);

                }
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

            //System.out.print ("inner join : " + roughTable.printTable());


        }


        return roughTable;

    }

    public static ArrayList<String> makeMultRow(Table t1, Table t2, int t1RowInd, int t2RowInd, ArrayList<String> a) {
        ArrayList<String> newEditedRow = new ArrayList<>();

        for (String sameName : a) {
            String item = t1.getLinkedMap().get(sameName).getValues().get(t1RowInd);
            newEditedRow.add(item);


        }

        for (Column c1: t1.getLinkedMap().values()) {
            if (!(containsItem(a, c1.getName()))) {
                String item = t1.getLinkedMap().get(c1.getName()).getValues().get(t1RowInd);
                newEditedRow.add(item);
            }
        }

        for (Column c2 : t2.getLinkedMap().values()) {
            if (!(containsItem(a, c2.getName()))) {
                String item = t2.getLinkedMap().get(c2.getName()).getValues().get(t2RowInd);
                newEditedRow.add(item);

            }

        }

        return newEditedRow;

    }

    public static boolean canJoinMultiple(Table t1, Table t2, int t1RowNum, int t2RowNum, ArrayList<String> sameCols) {
        int count = 0;
        for (String sameColName : sameCols) {
            String valueT1 = t1.getLinkedMap().get(sameColName).getValues().get(t1RowNum);
            String valueT2 = t2.getLinkedMap().get(sameColName).getValues().get(t2RowNum);
            if (!(valueT1.equals(valueT2))) {
                return false;

            }

        }
        return  true;
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
        ArrayList<ArrayList<String>> temp1 = new ArrayList<>(colOrder);
        for (Column c1 : t1.getLinkedMap().values()) { //add the names of the unique columns in table 1, starting from
            // the left most column
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
        for (String t1Key : this.getLinkedMap().keySet()) {
            if (t2.getLinkedMap().keySet().contains(t1Key)) {
                return false;
            }
        }
        return true;

    }
    public void addRow(ArrayList<String> rowToAdd) {
        int count = 0;
        for (Column c : this.getLinkedMap().values()) {
            String valueToAdd = rowToAdd.get(count);
            c.addVal(valueToAdd);
            count += 1;
        }


    }

    public static ArrayList<String> makeCartesianRow(Table t1, Table t2, int rowNumT1, int rowNumT2) {
        //returns a full row, at a certaing index between 2 tables
        ArrayList<String> newRow = new ArrayList<>();

        for (Column c : t1.getLinkedMap().values()) {
            String item = c.getValues().get(rowNumT1);
            newRow.add(item);
        }
        for (Column c : t2.getLinkedMap().values()) {
            String item = c.getValues().get(rowNumT2);
            newRow.add(item);
        }
        return newRow; //does this return null?
    }
    private static boolean equalColVals(Column c1, Column c2, int index) {
        if (!((c1.getMyType()).equals(c2.getMyType()))) {
            return false;
        } else {
            return c1.getValues().get(index).equals(c2.getValues().get(index));
        }


    }
    private static ArrayList<String> getCommonValues(Column c1, Column c2) { //get common values between two columns
        ArrayList<String> commonValues = new ArrayList<>();
        for (int i = 0; i < c1.getValues().size(); i++) {
            if (equalColVals(c1, c2, i)) {
                commonValues.add(c1.getValues().get(i));
            }
        }

        return commonValues;
    }

    public static ArrayList<String> getSameColumnNames(Table t1, Table t2) {
        ArrayList<String> similarColNames = new ArrayList<>();
        for (Column c1 : t1.getLinkedMap().values()) {
            if (t2.getLinkedMap().keySet().contains(c1.getName())) {
                similarColNames.add(c1.getName());
            }
        }
        return similarColNames;
    }

    public void removeRows(ArrayList<Integer> rowIndicesToRemove) {
        //LinkedHashMap<String, Column> copyMap = new LinkedHashMap<String, Column>(this.getLinkedMap());
        //it's ok, cux we need to change the actual hMap
        for(int rowIndex : rowIndicesToRemove) {
            for (Column col : colMap.values()) {
                col.getValues().remove(rowIndex); //is this ok that columns change as loop progresses?
            }

        }
    }



    public String insertRow(String vals) {
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



    public LinkedHashMap<String, Column> getLinkedMap() {
        return colMap;
    }

    public ArrayList<String> getColNames() {
        return colNames;
    }
}
