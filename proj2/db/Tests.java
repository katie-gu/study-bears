package db;

import java.io.IOException;

/**
 * Created by jhinukbarman on 2/27/17.
 */
public class Tests {
    public static Database da = new Database();
    public static Parser p = new Parser(da);

    public static void testLoadTable() throws IOException{
        p.loadTable("examples/t1");
        p.loadTable("examples/t2");
       // p.printTable("t1");
        p.eval("select * from examples/t1, examples/t2");

        //System.out.print("hi");
    }

   // public static void testJoin() {
    //    p.select("* from t1, t2");
   // }
    public static void testCreateTable() {
        p.eval("load examples/t1");
        p.eval("create table examples/t1");

    }


    public static void main(String[] args) throws IOException{
        testLoadTable();
        testCreateTable();
        //testJoin();
    }
}
