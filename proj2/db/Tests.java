package db;

import java.io.IOException;

/**
 * Created by jhinukbarman on 2/27/17.
 */
public class Tests {
    private static Database da = new Database();
    private static Parser p = new Parser(da);

    public static void testLoadTable() throws IOException {
        p.loadTable("examples/fans");
        //p.loadTable("examples/t2");
        //p.loadTable("examples/fans");
       // p.printTable("t1");

        //System.out.print("hi");
    }

    public static void testSelect() {
        p.eval("select * from examples/t1, examples/t2, examples/fans");
    }


   // public static void testJoin() {
    //    p.select("* from t1, t2");
   // }
    public static void testCreateTable() {
        p.eval("load examples/t1");
        p.eval("create table examples/t1");

    }


    public static void main(String[] args) throws IOException {
        testLoadTable();
        testSelect();
        //testCreateTable();
        //testJoin();
    }
}
