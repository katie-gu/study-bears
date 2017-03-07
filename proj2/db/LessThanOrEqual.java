package db;

import java.util.ArrayList;
/**
 * Created by jhinukbarman on 3/6/17.
 */
public class LessThanOrEqual extends ComparisonOperators{
    private String split1;
    private String split2;
    private String operand;
    private Table t;
    private Table curr;
    private String condition;

    public LessThanOrEqual(String split1, String split2, String operand, Table t, Table curr, String condition) {
        this.split1 = split1;
        this.split2 = split2;
        this.operand = operand;
        this.t = t;
        this.curr = curr;
        this.condition = condition;
    }


    @Override
    public Table evalOp() {
        //dont need to check the type because it should all be the same ????
        ArrayList<Integer> removeRowIndices = new ArrayList<>();
        Column c = t.getLinkedMap().get(split1);

        if (condition.equals("Unary")) {
            if (isValidStringOperand(split1, split2, operand, curr)) {
                //means that both column type and split2 are strings.
                for (int i = 0; i < c.getValues().size(); i++) {
                    if (c.getValues().get(i).compareTo(split2) < 0) {
                        removeRowIndices.add(i);
                    }
                }

                t.removeRows(removeRowIndices);

                return t;
            } else {
                float f = Float.parseFloat(split2);

                for (int i = 0; i < c.getValues().size(); i++) {
                    float colVal = Float.parseFloat(c.getValues().get(i));
                    if (colVal > f) {
                        removeRowIndices.add(i);
                    }
                }

                t.removeRows(removeRowIndices);

                return t;
            }
        } else {
            Column c2 = t.getLinkedMap().get(split2);
            if (c.getMyType().equals("string")) {
                //means that both column type and split2 are strings.
                for (int i = 0; i < c.getValues().size(); i++) {
                    if (c.getValues().get(i).compareTo(c2.getValues().get(i)) < 0) {
                        removeRowIndices.add(i);
                    }
                }

                t.removeRows(removeRowIndices);

                return t;
            } else {

                for (int i = 0; i < c.getValues().size(); i++) {
                    float f = Float.parseFloat(c2.getValues().get(i));
                    float colVal = Float.parseFloat(c.getValues().get(i));
                    if (colVal > f) {
                        removeRowIndices.add(i);
                    }
                }

                t.removeRows(removeRowIndices);

                return t;
            }

        }


    }

    //return new Table("newT");


}
