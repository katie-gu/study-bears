package db;

/**
 * Created by jhinukbarman on 3/5/17.
 */
public interface ArithmeticOperators {
    Column combineCols(Column a, Column b);

    default boolean checkValid (Column a, Column b) {
        if ((a.getMyType().equals("string") && (!b.getMyType().equals("string")))
                || (b.getMyType().equals("string") && (!a.getMyType().equals("string")))) {

            return false;
        }
        return true;

    }

}
