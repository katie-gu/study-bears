package db;

import java.io.IOException;

/**
 * Created by jhinukbarman on 2/27/17.
 */
public class Tests {
    public static Database da = new Database();
    public static Parser p = new Parser(da);

    public static void testLoadTable() throws IOException{
        p.loadTable("t1");
        p.loadTable("t2");
        p.printTable("t1");

        //System.out.print("hi");
    }

   // public static void testJoin() {
    //    p.select("* from t1, t2");
   // }
    public static void printTable() {
    }


    public static void main(String[] args) throws IOException{
        testLoadTable();
        //testJoin();
    }
}
