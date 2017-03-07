package db;

import java.util.ArrayList;
/**
 * Created by jhinukbarman on 3/6/17.
 */
public class NotEqualTo extends ComparisonOperators{
    private String split1;
    private String split2;
    private String operand;
    private Table t;
    private Table curr;

    public NotEqualTo(String split1, String split2, String operand, Table t, Table curr) {
        this.split1 = split1;
        this.split2 = split2;
        this.operand = operand;
        this.t = t;
        this.curr = curr;
    }


    @Override
    public Table evalOp() {
        //dont need to check the type because it should all be the same ????
        ArrayList<Integer> removeRowIndices = new ArrayList<>();
        Column c = t.getLinkedMap().get(split1);

        if (isValidStringOperand(split1, split2, operand, curr)) {
            //means that both column type and split2 are strings.
            for (int i = 0; i < c.getValues().size(); i++) {
                if (c.getValues().get(i).equals(split2)) {
                    removeRowIndices.add(i);
                }
            }

            t.removeRows(removeRowIndices);

            return t;
        } else {
            float f = Float.parseFloat(split2);

            for (int i = 0; i < c.getValues().size(); i++) {
                float colVal = Float.parseFloat(c.getValues().get(i));
                if (colVal == f) {
                    removeRowIndices.add(i);
                }
            }

            t.removeRows(removeRowIndices);

            return t;
        }
    }

    //return new Table("newT");


}
