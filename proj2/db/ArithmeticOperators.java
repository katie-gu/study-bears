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

    public Column combineUnaryCols() { return new Column("c", "string"); }

    public static boolean isSameColType(Column a, Column b) {
        if (a.getMyType().equals("string") && (b.getMyType().equals("string"))) {
            return true;
        } else if (a.getMyType().equals("float") && ((b.getMyType().equals("float")) || b.getMyType().equals("int"))){
            return true;
        } else if (b.getMyType().equals("float") && ((a.getMyType().equals("float")) || a.getMyType().equals("int"))){
            return true;
        } else {
            return false;
        }
    }

    //do conditional checks in parser?
    public boolean checkValid(Column a, Column b) {


        if (((a.getMyType().equals("string")) && (!b.getMyType().equals("string")))
                || (b.getMyType().equals("string") && (!a.getMyType().equals("string")))) {

            return false;
        }
        return true;

    }

    public boolean isValidType(String type1, String actual) {
        String actualType = "";
        if (actual.contains("'")) {
            actualType = "string";
            if (type1.equals(actualType)) {
                return true;
            }
        } else {
            if (type1.equals("string")) {
                return false;
            }
        }

        return true;

    }

}
