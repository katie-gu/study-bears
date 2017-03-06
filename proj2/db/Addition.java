package db;

/**
 * Created by jhinukbarman on 3/5/17.
 */
public class Addition implements ArithmeticOperators{
    @Override
    public Column combineCols(Column a, Column b) {
        Column newCol;
        if (checkValid(a, b)) {
            if (a.getMyType().equals("string")) {
                for (String s : a.getValues()) {

                }
            }
        }
       // for ()
        return new Column("name", "type"); //replace this line
    }
}
