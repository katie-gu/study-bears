package db;

/**
 * Created by jhinukbarman on 3/5/17.
 */
public class ArithmeticOperators {
    private Column c1;
    private Column c2;

    public ArithmeticOperators() {

    }


    public ArithmeticOperators(Column a, Column b) {
        c1 = a;
        c2 = b;
    }

    public Column combineCols() {
        return new Column("c", "string");
    }



    //do conditional checks in parser?
    public boolean checkValid(Column a, Column b) {


        if (((a.getMyType().equals("string")) && (!b.getMyType().equals("string")))
                || (b.getMyType().equals("string") && (!a.getMyType().equals("string")))) {

            return false;
        }
        return true;

    }

}
