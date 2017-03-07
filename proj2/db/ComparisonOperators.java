package db;

/**
 * Created by jhinukbarman on 3/6/17.
 */
public class ComparisonOperators {
    public ComparisonOperators() {

    }

    public Table evalOp() {
        return new Table("newT");
    }

    public boolean isValidStringOperand(String split1, String split2, String operand, Table t) {
        boolean isValid = false;
        String splitType = "";
        if (split2.contains("'")) {
            splitType = "string";

            if (splitType.equals(t.getLinkedMap().get(split1).getMyType())) {
                isValid = true;
            }

        }
        return isValid;
    }
}
