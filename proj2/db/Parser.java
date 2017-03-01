package db;

//import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.nio.file.*;
//import java.nio.charset.*;

public class Parser {
    // Various common constructs, simplifies parsing.
    public static Database d;
    public Parser(Database db){
        d = db;
    }
    private static final String REST  = "\\s*(.*)\\s*",
                                COMMA = "\\s*,\\s*",
                                AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
                                 LOAD_CMD   = Pattern.compile("load " + REST),
                                 STORE_CMD  = Pattern.compile("store " + REST),
                                 DROP_CMD   = Pattern.compile("drop table " + REST),
                                 INSERT_CMD = Pattern.compile("insert into " + REST),
                                 PRINT_CMD  = Pattern.compile("print " + REST),
                                 SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*" +
                                               "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
                                 SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                                               "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                                               "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+" +
                                               "[\\w\\s+\\-*/'<>=!]+?)*))?"),
                                 CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                                                   SELECT_CLS.pattern()),
                                 INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                                               "\\s*(?:,\\s*.+?\\s*)*)");

    public static void main(String[] args) {
        /*
        if (args.length != 1) {
            System.err.println("Expected a single query argument");
            return;
        }

        eval(args[0]);
        */
        Database da = new Database();
        Parser p = new Parser(da);
        p.loadTable("fans");
        System.out.print("hi");
    }

    public static void eval(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
             createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
             loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
             storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
             dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
             insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
             printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
             select(m.group(1));
        } else {
            System.err.printf("Malformed query: %s\n", query);
        }
    }

    private static void createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }
    }

    private static void createNewTable(String name, String[] cols) {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < cols.length-1; i++) {
            joiner.add(cols[i]);
        }

        String colSentence = joiner.toString() + " and " + cols[cols.length-1];
        System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);
        //storeTable(name, colSentence);
    }

    private static void createSelectedTable(String name, String exprs, String tables, String conds) {
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
    }

    private static void loadTable(String name) {
        // "examples/" + name + ".tbl"
       // d.h.put(name, );
        //stack overflow - cite
        Table t = new Table(name);

        String pathName = "/Users/jhinukbarman/cs61b/aej/proj2/examples/fans.tbl";

        File f = new File(pathName);

        /*
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(name) && name.endsWith(".tbl");
            }
        });

        for (File val : matchingFiles) {
            //String valString = FileUtils.readFileToString(val);
            //System.out.println(val);
            //System.out.println("****");
            //System.out.println(pathName + name + ".tbl");

            String myFile = pathName + name + ".tbl";
            String fileString = val.toString();
            if (fileString.equals(myFile)) {
                String fileName = name + ".tbl";
                parseTable(fileString, t);
                //d.getMap().
                t.printTable();
                System.out.println(name + ".tbl");
                break;
                //d.h.put(name, name + ".tbl");
            }

        }
        */
        //System.out.printf("You are trying to load the table named %s\n", name);
        parseTable(f.toString(), t);
        System.out.print("hello its me");
        d.addTable(t.tableName, t);
        System.out.println("now table contents....");
        d.getMap().get(t.tableName).printTable();
        System.out.println(d.getMap().get(t.tableName));
    }



    private static void parseTable(String fileName, Table t){
        //InputStream i = new FileInputStream(fileName);
        String contents = "";
        //do if statements instead of try catch
        try {
            //citation stack overflow
            contents = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException ie){
            System.out.println("ERROR: IOE Exception. Cannot convert file contents to string");
        }

        System.out.println(contents);

        //File file = new File(fileName);
       // String lines[] = new String[10];
       // String currentColumn;

        int tokenIndex = 0;
        String lineToken;
        StringTokenizer st = new StringTokenizer(contents, "\r\n");
            while (st.hasMoreTokens()) {
                lineToken = st.nextToken();
                System.out.println(lineToken);
                parseLine(lineToken, tokenIndex, t);
                tokenIndex += 1;
            }

    }

    private static void parseLine(String line, int index, Table t){
        String token;
        StringTokenizer stL = new StringTokenizer(line, ",");
        int colNum = 0;
        while (stL.hasMoreTokens()) {
            if (index == 0){
                token = stL.nextToken();
                parseColumnHead(token, t);
                //parse column heads
            } else {
                token = stL.nextToken();
                insertValue(token, t, colNum);
                colNum += 1;
                //do the rest
            }
        }

    }

    private static void insertValue(String myToken, Table t, int cNum) {
        //not necessary if we input already in parse columnhead method
        //ArrayList<String> colNames = new ArrayList<>();
       // for (String key : t.getLinkedMap().keySet()) {
        //    colNames.add(key);
       // }

        String correctColName = t.getColNames().get(cNum); //x
        for (String key : t.getLinkedMap().keySet()) {
            if (key.equals(correctColName)) {
                t.getLinkedMap().get(key).addVal(myToken);
            }
        }


    }

    private static void parseColumnHead(String s, Table t) {
        String splittedColHead[] = s.split("\\s+");
        String colName = splittedColHead[0];
        String colType = splittedColHead[1];
        createColumn(t, colName, colType);
    }

    private static void createColumn(Table t, String cName, String cType){
        Column c = new Column(cName, cType);
        t.getLinkedMap().put(cName, c);
        System.out.println(t.getColNames());
        t.getColNames().add(cName);
    }

    private static void storeTable(String name) {
        //write to a file with name
        String pathName = "/Users/jhinukbarman/cs61b/aej/proj2/examples/";
        File f = new File(pathName);
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(name) && name.endsWith(".tbl");
            }
        });
        for (File val: matchingFiles){
            if (val.equals(pathName + name + ".tbl")){

            }
        }

        System.out.printf("You are trying to store the table named %s\n", name);
    }

    private static void storeTable(String name, String colSentence) {
        //write to a file with name

        System.out.printf("You are trying to store the table named %s\n", name);
    }

    private static void dropTable(String name) {
        System.out.printf("You are trying to drop the table named %s\n", name);
    }

    private static void insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed insert: %s\n", expr);
            return;
        }

        System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
    }

    private static void printTable(String name) {
        System.out.printf("You are trying to print the table named %s\n", name);
    }

    private static void select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);
            return;
        }

        select(m.group(1), m.group(2), m.group(3));
    }

    private static void select(String exprs, String tables, String conds) {
        System.out.printf("You are trying to select these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
    }
}
