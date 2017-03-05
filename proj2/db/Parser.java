package db;

//import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.io.FileReader;
import java.nio.file.*;
//import java.nio.charset.*;

public class Parser {
    // Various common constructs, simplifies parsing.
    public static Database d;

    public Parser(Database db) {
        d = db;
    }

    private static final String REST = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD = Pattern.compile("load " + REST),
            STORE_CMD = Pattern.compile("store " + REST),
            DROP_CMD = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");

    /*
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Expected a single query argument");
            return;
        }

        eval(args[0]);

        Database da = new Database();
        Parser p = new Parser(da);
        p.loadTable("fans");
        System.out.print("hi");
    }
    */

    public static String eval(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            return loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        } else {
            //return "ERROR: incorrect command";
            //System.err.printf("Malformed query: %s\n", query);
        }
        return "ERROR: incorrect command";

    }

    private static String createTable(String expr) { //expr : t0 (x string,y int,z float)
        //create table <table name> (<column0 name> <type0>, <column1 name> <type1>, ...)
        //do create table before insertRow
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            return createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            return createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            //System.err.printf("Malformed create: %s\n", expr);
            return "ERROR: malformed command";
        }
    }

    private static String createNewTable(String name, String[] cols) { // name : t0, cols = {'x int', 'y int', 'z int'}
        if (d.getMap().containsKey(name)) {
            return "ERROR: cannot create table with same name as an existing table.";
        }
        // if (cols.length == 0) {
        //return "ERROR: cannot create table with no columns.";
        // }
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < cols.length - 1; i++) {
            joiner.add(cols[i]);
        }

        String colSentence = joiner.toString() + "," + cols[cols.length - 1];
        //colSentence = 'x string,y int,z int"
        Table createdTable = new Table(name);
        for (String col : cols) {
            //split string. add to colname and type in new table

            String splittedColHead[] = col.split("\\s+");
            String colName = splittedColHead[0];
            String colType = splittedColHead[1];
            if ((!(colType.equals("string"))) && (!(colType.equals("float"))) && (!(colType.equals("int")))) {
                return "ERROR: invalid column type";
            }
            createdTable.getLinkedMap().put(colName, new Column(colName, colType));
            createdTable.getColNames().add(colName);
        }
        d.addTable(name, createdTable);
        //createdTable.printTable();
        //createTable.
        //System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);
        return "";
        //storeTable(name, colSentence);
    }

    private static String createSelectedTable(String name, String exprs, String tables, String conds) {
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
        return "";
    }

    public static String loadTable(String name) {
        Table t = new Table(name);
        File file = new File(name + ".tbl");
        if (file.exists()) {
            parseTable(name, t);
            d.addTable(name, t);
            return "";
        }
        return "ERROR: Invalid file input";
    }

    private static void parseTable(String fileName, Table t) {
        String contents = "";

        try {
            InputStream in = new FileInputStream((fileName + ".tbl"));
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(in));
            String line = buffReader.readLine();
            StringBuilder sBuild = new StringBuilder();
            while (line != null) {
                sBuild.append(line).append("\n");
                line = buffReader.readLine();
            }
            contents = sBuild.toString();
            in.close();
        } catch (IOException ie) {
            System.out.println("ERROR: invalid.");
        }

        int tokenIndex = 0;
        String lineToken;
        if (contents.equals("")) {
            System.out.println("ERROR: malformed table.");
        }
        StringTokenizer st = new StringTokenizer(contents, "\n");
        while (st.hasMoreTokens()) {
            lineToken = st.nextToken();
            parseLine(lineToken, tokenIndex, t);
            tokenIndex += 1;
        }

    }

    private static void parseLine(String line, int index, Table t) {
        String token;
        StringTokenizer stL = new StringTokenizer(line, ",");
        int colNum = 0;
        while (stL.hasMoreTokens()) {
            if (index == 0) {
                token = stL.nextToken();
                parseColumnHead(token, t);
            } else {
                token = stL.nextToken();
                insertValue(token, t, colNum);
                colNum += 1;
                //do the rest
            }
        }

    }

    private static void insertValue(String myToken, Table t, int cNum) {
        if (cNum >= t.getColNames().size()) {
            System.out.print("ERROR: malformed table.");
        }
        String correctCol = t.getColNames().get(cNum);
        for (String key : t.getLinkedMap().keySet()) {
            if (key.equals(correctCol)) {
                t.getLinkedMap().get(key).addVal(myToken);
                break;
            }
        }

    }

    private static String parseColumnHead(String s, Table t) {
        String splittedColHead[] = s.split("\\s+");
        if (splittedColHead.length != 2) {
            return "ERROR: Malformed table";
        }
        String colName = splittedColHead[0];
        String colType = splittedColHead[1];
        createColumn(t, colName, colType);
        return "";
    }

    private static void createColumn(Table t, String cName, String cType) {
        Column c = new Column(cName, cType);
        t.getLinkedMap().put(cName, c);
        t.getColNames().add(cName);
    }

    private static String storeTable(String name) {
        if ((d.getMap() == null) || (!(d.getMap().containsKey(name)))) {
            return "ERROR: table does not exist in database";
        }
        //Table t = new Table(name);
        String fileName = name + ".tbl";

        try {
            String verify, putData;
            File file = new File(fileName);
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(d.getMap().get(name).printTable());
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    //private static void storeTable(String name, String colSentence) {
        //write to a file with name

        //System.out.printf("You are trying to store the table named %s\n", name);
    //}

    private static String dropTable(String name) {
        if(d.getMap() == null) {
            return "ERROR: No tables exist.";
        } else if (!(d.getMap().keySet().contains(name))) {
            return "ERROR: table does not exist in database.";
        } else {
            d.getMap().remove(name);
            return "";
        }
        //System.out.printf("You are trying to drop the table named %s\n", name);
    }

    private static String insertRow(String expr) { // expr : examples/t1 values 1,2,3,'hi bye'
        Matcher m = INSERT_CLS.matcher(expr);
        String splittedExpr[] = expr.split("\\s+values\\s+"); //cannot split by spaces!
        if (splittedExpr.length <= 1) {
            return "ERROR: Malformed list";
        }
        String table = splittedExpr[0];
        String actualValues = splittedExpr[1];
        if (!m.matches()) {
            System.err.printf("Malformed insert: %s\n", expr);
            return "ERROR: malformed insert";
        } else if (d.getMap() == null || !(d.getMap().keySet().contains(table))) {
            return "ERROR: Cannot insert row into nonexistent table.";
        } else {
            return d.getMap().get(table).insertRow(actualValues);
        }
       // System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
    }

    public static String printTable(String name) {
        if (d.getMap().get(name) == null) {
            return "ERROR: Table does not exist.";
        } else {
            return d.getMap().get(name).printTable();
        }
       // System.out.printf("You are trying to print the table named %s\n", name);
    }

    //separates expr by calling the overloaded select method
    //changed to public for jUnit test
    public static String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);
            return "ERROR: malformed select";
        }

        return select(m.group(1), m.group(2), m.group(3));
    }

    private static String select(String exprs, String tables, String conds) {
        tables = tables.replaceAll("\\s+","");
        String splittedTables[] = tables.split(","); //may change back again
        String table1 = splittedTables[0];
        String table2 = splittedTables[1];
        //check my code on github

        Table t1 = d.getMap().get(table1);
       // System.out.println(t1.printTable());
       // System.out.println(t1.getColNames());

        Table t2 = d.getMap().get(table2);
        Table joinedTable = new Table("t3"); //change name later
        //if ((t1.getColNames().size() == 0) || (t2.getColNames().size() == 0)) {
            //return "ERROR: Cannot select from nonexistent tables.";
       // } else {
            if (exprs.equals("*")) {
                joinedTable = t1.join(t2, joinedTable);
            }
       // }
        return joinedTable.printTable();


    }
    // System.out.printf("You are trying to select these expressions:" +
    //       " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);






    //do Cartesian join method
    //do inner join method
       // System.out.printf("You are trying to select these expressions:" +
         //       " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);






    //do Cartesian join method
    //do inner join method

    public static boolean equalColVals (Column c1, Column c2, int index) {
        if(!(c1.getMyType().equals(c2.getMyType()))) {
            return false;
        }
        return (c1.getValues().get(index).equals(c2.getValues().get(index)));
    }

    public static ArrayList<String> getCommonValues(Column c1, Column c2) {
        ArrayList<String> commonValues = new ArrayList<>();
        for (int i = 0; i < c1.myValues.size(); i++) {
            if (equalColVals(c1, c2, i)) {
                commonValues.add(c1.getValues().get(i));
            }
        }
        return commonValues;
    }
}
