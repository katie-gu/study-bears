package db;

//import java.nio.file.Files;
//import java.lang.reflect.Array;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.Arrays;
//import java.io.FileReader;
//import java.nio.file.*;
//import java.nio.charset.*;

public class Parser {
    // Various common constructs, simplifies parsing.
    private static Database d;

    private static Table selectedTable;

    public Parser(Database db) {
        d = db;
    }

    //added this method
    public Database getDataBase() {
        return d;
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
    private static final Pattern CREATE_NEW = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*"
            + "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+"
                    + "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+"
                    + "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+"
                    + "[\\w\\s+\\-*/'<>=!]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as select\\s+"
                    + SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?"
                    + "\\s*(?:,\\s*.+?\\s*)*)");


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
        //} else {
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
        //System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
              //  " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
        select(exprs, tables, conds);
        d.getMap().put(name, selectedTable);
        if (selectedTable == null) {
            return "ERROR";
        }
        //selectedTable.changeName(name);
        //selectedTable.getName()

        return "";
    }

    public static String loadTable(String name) {
        Table t = new Table(name);
        File file = new File(name + ".tbl");
        if (file.exists()) {
            d.addTable(name, t);
            parseTable(name, t);
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
        //http://stackoverflow.com/questions/26251039/saving-files-in-current-directory
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
        if (d.getMap() == null) {
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
        String alias = ""; //where y>5 ---> conds: y>5
        int random = (int) (Math.random() * 100 + 1);
        Table tempTable = new Table("tempTable" + random);//random name

        tables = tables.replaceAll("\\s+", "");
        if (conds != null) {
            conds = conds.replaceAll("\\s+", "");
        }
        String splittedTables[] = tables.split(","); //may change back again

        //either join table or pick single table if choosing from only 1 table
        if (splittedTables.length <= 1) {
            tempTable = d.getMap().get(tables);
            if (tempTable == null) {
                return "ERROR: table non existent";
            }
        } else {
            //checks if all tables in the input of tables are valid
            //if (joinedTable(tables).getName().equals("Invalid Table")) {
              //  return "ERROR: cannot select from nonexistent tables.";
            //} else {
                tempTable = joinedTable(tables);
           // }
        }

        if (tempTable.getName().equals("Invalid Table")) {
            return "ERROR : Invalid table";
        }

        //conds --> don't need else for this
        if (conds != null) {
            tempTable = editedTable(conds, tempTable);
        }

        if (tables.equals("")) {
            //System.out.println("ERROR: malformed table.");
            return "ERROR: malformed table.";
        }

        if (tempTable.getName().equals("ErroredTable")) {
            return "ERROR: column types of condition do not match";
        }

        //exprs != null????
        if (!(exprs.equals("*"))) {
            tempTable = editedExprsTable(exprs, tempTable, alias);
        }


        //-----------------------//
        /*
        ArrayList<String> operands = new ArrayList<>();
        operands.add("+");
        operands.add("-");
        operands.add("*");
        operands.add("/");

        String alias = ""; //a
        String exprActual = ""; //x+y
        tables = tables.replaceAll("\\s+", ""); //select x from t1;

        Table joinedTable = new Table("t3"); //change name later
        ArrayList<String> exprsArr = new ArrayList<String>();
        exprs = exprs.replaceAll("\\s+", "");

        //expressions Array list (of column names desired)
        if (exprs.contains(",")) {
            StringTokenizer colNames = new StringTokenizer(exprs, ",");
            String token = "";
            while (colNames.hasMoreTokens()) {
                token = colNames.nextToken();
                exprsArr.add(token);
            }
        } else {
            exprsArr.add(exprs);
        }

        //tokenize the tables ( multiple table join)
        // int tokenIndex = 0;


        StringTokenizer st = new StringTokenizer(tables, ",");
        String t1Name = st.nextToken(); //t1
        Table t1 = d.getMap().get(t1Name);

        //new line added :)
        if (!(st.hasMoreTokens())) {
            if (exprs.equals("*")) {
                selectedTable = t1;
                //if (conds != null)
                return t1.printTable();
            }
            if (exprsArr.size() > t1.getColNames().size()) {
                return "ERROR: Too many columns inputted";
            }
            if (!(isColExistent(exprsArr, t1))) {
                return "ERROR: column not existent";
            }
            selectedTable = specificSelect(exprsArr, t1);

            //CHECK IF CONDS NOT NULL

            return selectedTable.printTable();
            //return t1.printTable();
        }
        String t2Name = st.nextToken();
        Table t2 = d.getMap().get(t2Name);

        if (t1Name.equals(t2Name)) {
            //not sure if this works if combined table is same as 3rd table
            return t1.printTable();
        }

       // if (exprs.equals("*")) {
        joinedTable = t1.join(t2, joinedTable);
            //System.out.print("outer join : " + joinedTable.printTable());
        Table prevToken = joinedTable;

        while (st.hasMoreTokens()) {



        */

        return tempTable.printTable();
    }

    public static Table editedExprsTable (String exprs, Table input, String alias) {
        ArrayList<String> operands = new ArrayList<>((Arrays.asList("+", "-", "/", "*")));
        //String splittedExpr[] =

       // if (exprs.contains(" as ")) {
            return evalExprWithAs(operands, exprs, input, alias);
       // }

       // System.out.println("WTF");
       // return new Table("hi");


    }

    public static Table evalExprWithAs(ArrayList<String> operands, String exprs, Table t, String alias) {
        Table n = new Table("newCombinedT");
        String splittedExpr[] = exprs.split("\\s+" + "as" + "\\s+"); //may change back again
        exprs = exprs.replaceAll("\\s+", "");
        exprs = splittedExpr[0];
        if (splittedExpr.length > 1) {
            alias = splittedExpr[1];
        }

        exprs = exprs.replaceAll("\\s+", "");
        String splitExpr[] = exprs.split(",");
        String aliasTemp = alias;

        for (String expr : splitExpr) {
            boolean containsOperand = false;
            if (splitExpr.length > 1) {
                for (String operand : operands) {
                    if ((expr.contains(operand))) {
                        containsOperand = true;
                        break;
                    }
                }
                if (!(containsOperand)) {
                    //alias = expr;
                    Column combinedCol = colFilter(operands, expr, t, expr);
                    n.getLinkedMap().put(expr, combinedCol);
                    n.getColNames().add(expr);
                } else {
                    Column combinedCol = colFilter(operands, expr, t, alias);
                    n.getLinkedMap().put(alias, combinedCol);
                    n.getColNames().add(alias);
                }

                //if (alias.equals("")) {
                 //   alias = expr;
               // } //else {
                //  int random = (int) (Math.random() * 100 + 1);
                //    alias += random;
                //  }
            } else {
                Column combinedCol = colFilter(operands, expr, t, alias);
                n.getLinkedMap().put(alias, combinedCol);
                n.getColNames().add(alias);
            }

        }

        return n;

    }


    public static Table copyTable(Table first) {
        Table newT = new Table("copy" + first.getName());
        LinkedHashMap<String, Column> copyMap = new LinkedHashMap<>(first.getLinkedMap());
        newT.colMap = copyMap; //
        for (String col : first.getColNames()) {
            newT.getColNames().add(col);
        }
        return newT;

    }
    public static Table editedTable(String conds, Table currTable) {
        Table newTable = copyTable(currTable);
        String splittedConds[] = conds.split(",");
        //change split earlier
        if (conds.contains("and")) {
            splittedConds = conds.split("and");
        } else {
            splittedConds = conds.split(",");
        }
        ArrayList<String> condArr = new ArrayList<>((Arrays.asList("==", "!=", "<=", ">=", "<", ">")));
        //String condArr[] = {"==", "!=", "<", ">", "<=", ">="};

        for (String condition : splittedConds) {
            for (String condOp : condArr) {
                if (condition.contains(condOp)) {
                    newTable = evalOp(condition, condOp, newTable, currTable);
                    break;
                }
            }
        }

        return newTable;


    }

    public static Table evalOp(String condLine, String operand, Table t, Table curr) {
        ComparisonOperators op;
        condLine = condLine.replaceAll("\\s+", "");
        String splittedCondLine[] = condLine.split("\\" + operand);
        String split1 = splittedCondLine[0];
        String split2 = splittedCondLine[1];

        if (operand.equals("==")) {
            if (isUnary(t, split2)) {
                op = new EqualTo(split1, split2, operand, t, curr, "Unary");
                return op.evalOp();
            } else {
                if (isSameColType(t, split1, split2)) {
                    op = new EqualTo(split1, split2, operand, t, curr, "Binary");
                    return op.evalOp();
                }
            }

        } else if (operand.equals("!=")) {
            if (isUnary(t, split2)) {
                op = new NotEqualTo(split1, split2, operand, t, curr, "Unary");
                return op.evalOp();
            } else {
                if (isSameColType(t, split1, split2)) {
                    op = new NotEqualTo(split1, split2, operand, t, curr, "Binary");
                    return op.evalOp();
                }
            }
        } else if  (operand.equals("<")) {
            if (isUnary(t, split2)) {
                op = new LessThan(split1, split2, operand, t, curr, "Unary");
                return op.evalOp();
            } else {
                if (isSameColType(t, split1, split2)) {
                    op = new LessThan(split1, split2, operand, t, curr, "Binary");
                    return op.evalOp();
                }
            }
        } else if  (operand.equals(">")) {
            if (isUnary(t, split2)) {
                op = new GreaterThan(split1, split2, operand, t, curr, "Unary");
                return op.evalOp();
            } else {
                if (isSameColType(t, split1, split2)) {
                    op = new GreaterThan(split1, split2, operand, t, curr, "Binary");
                    return op.evalOp();
                }
            }
        } else if  (operand.equals("<=")) {
            if (isUnary(t, split2)) {
                op = new LessThanOrEqual(split1, split2, operand, t, curr, "Unary");
                return op.evalOp();
            } else {
                if (isSameColType(t, split1, split2)) {
                    op = new LessThanOrEqual(split1, split2, operand, t, curr, "Binary");
                    return op.evalOp();
                }
            }
            // } else {
        } else if  (operand.equals(">=")) {
            if (isUnary(t, split2)) {
                op = new GreaterThanOrEqual(split1, split2, operand, t, curr, "Unary");
                return op.evalOp();
            } else {
                if (isSameColType(t, split1, split2)) {
                    op = new GreaterThanOrEqual(split1, split2, operand, t, curr, "Binary");
                    return op.evalOp();
                }

            }
        }
        return new Table("ErrorTable");

        //return new Table("hi");

    }

    public static boolean isSameColType(Table t, String split1, String split2) {
        if (t.getLinkedMap().get(split1).getMyType().equals(t.getLinkedMap().get(split2).getMyType())) {
            return true;
        } else {
            return false;
        }
    }

    //public static boolean isBinary(Table t, String[] conds) {
     //   return t.getLinkedMap().keySet().contains(conds[2]);
        //if the item at the right of the operator position in array is a column name, then condition is binary
   // }

    public static boolean isUnary(Table t, String split2) {
        if(!(t.getLinkedMap().keySet().contains(split2))) {
            //if the item at the right of the operator position in array is not a column name and is a literal
            // then condition is binary

            return true;
        }
        return false;
        //if the item at the right of the operator position in array is a column name, then condition is binary
    }
    /*
    public static boolean isValidTables(String tables) {
        Table joinedTable = new Table("joinedTable"); //change name later

        StringTokenizer st = new StringTokenizer(tables, ",");
        String t1Name = st.nextToken(); //t1

        //CHECK IF T1 and T2 exist!
        Table t1 = d.getMap().get(t1Name);
        String t2Name = st.nextToken();
        Table t2 = d.getMap().get(t2Name);
        joinedTable = t1.join(t2, joinedTable);
        Table prevToken = joinedTable;

        while (st.hasMoreTokens()) {
            String currName = st.nextToken(); //t2
            Table currToken = d.getMap().get(currName);
            if ((prevToken == null) || (currToken == null)) {
                return false;
            } else {
                int random = (int) (Math.random() * 100 + 1);
                joinedTable = prevToken.join(currToken, new Table("t" + random));
                prevToken = joinedTable;
             }
        }

        return true;

    }
    */

    public static Table joinedTable(String tables) {
        Table joinedTable = new Table("joinedTable"); //change name later
        StringTokenizer st = new StringTokenizer(tables, ",");
        String t1Name = st.nextToken(); //t1
        Table t1 = d.getMap().get(t1Name);
        if (t1 == null) {
            return new Table("Invalid Table");
        }

        String t2Name = st.nextToken();
        Table t2 = d.getMap().get(t2Name);

        if (t2 == null) {
            return new Table("Invalid Table");
        }

        if (t1Name.equals(t2Name)) {
            //not sure if this works if combined table is same as 3rd table
            return t1;
        }

        joinedTable = t1.join(t2, joinedTable);
        Table prevToken = joinedTable;

        while (st.hasMoreTokens()) {
            String currName = st.nextToken(); //t2
            Table currToken = d.getMap().get(currName);
            if ((prevToken == null) || (currToken == null)) {
                return new Table("Invalid Table");
            } else {
                int random = (int) (Math.random() * 100 + 1);
                joinedTable = prevToken.join(currToken, new Table("t" + random));
                prevToken = joinedTable;
                d.getMap().put(joinedTable.getName(), joinedTable);
            }
        }

            return joinedTable;
    }





    public static Table specificSelect(ArrayList<String> colWanted, Table t) {
        Table newT = new Table("newTable");

        for (String col : colWanted) {
            Column colInput = t.getLinkedMap().get(col);
            newT.getLinkedMap().put(col, colInput);
            newT.getColNames().add(col);
        }

        return newT;
    }

    public static boolean isColExistent(ArrayList<String> colWanted, Table t) {
        boolean isCol = false;
        for (String col : colWanted) {
            if ((t.getColNames().contains(col))) {
                isCol = true;
            } else {
                isCol = false;
            }
        }
        return isCol;
    }

    public static Column colFilter(ArrayList<String> operands, String exprs, Table t, String aliasName) {
         ArithmeticOperators op;
         boolean ifUnary = false;

         for (String operand : operands) {
            if (exprs.contains(operand)) {
                //remove spaces from expression
                String splitOperand = "\\" + operand;
                String splittedCol[] = exprs.split(splitOperand); //may change back again

                if (splittedCol.length < 2) {
                    return new Column("NOCOL", "value");
                }
                String col1 = splittedCol[0];
                String col2 = splittedCol[1];

                Column c1 = t.getLinkedMap().get(col1);
                if (t.getLinkedMap().get(col2) == null) {
                    ifUnary = true;
                }
                Column c2 = t.getLinkedMap().get(col2);


                if (operand.equals("+")) {
                    if (ifUnary) {
                        op = new Addition(c1, col2, aliasName, "Unary");
                        return op.combineUnaryCols();
                    } else {
                        op = new Addition(c1, c2, aliasName);
                        return op.combineCols();
                    }

                } else if (operand.equals("-")) {
                    if (ifUnary) {
                        op = new Subtraction(c1, col2, aliasName, "Unary");
                        return op.combineUnaryCols();
                    } else {
                        op = new Subtraction(c1, c2, aliasName);
                        return op.combineCols();

                    }
                } else if (operand.equals("/")) {
                    if (ifUnary) {
                        op = new Division(c1, col2, aliasName, "Unary");
                        return op.combineUnaryCols();
                    } else {
                        op = new Division(c1, c2, aliasName);
                        return op.combineCols();
                    }
                } else {
                    if (ifUnary) {
                        op = new Multiplication(c1, col2, aliasName, "Unary");
                        return op.combineUnaryCols();
                    }
                    op = new Multiplication(c1, c2, aliasName);
                    return op.combineCols();
                }
            }

        }

       // if (exprs.contains(",")) {
           //  String splittedCol[] = exprs.split(splitOperand);
       // }
        return t.getLinkedMap().get(exprs);
    }


}
