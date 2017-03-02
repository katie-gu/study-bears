package db;

/**
 * Created by jhinukbarman on 2/27/17.
 */
public class Tests {
    public static Database da = new Database();
    public static Parser p = new Parser(da);

    public static void testLoadTable() {
        p.loadTable("t1");
        p.loadTable("t2");
        //System.out.print("hi");
    }

    public static void testJoin() {
        p.select("* from t1, t2");
    }


    public static void main(String[] args) {
        testLoadTable();
        testJoin();
    }
}
