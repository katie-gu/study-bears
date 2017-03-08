package db;

import java.util.ArrayList;
/**
 * Created by jhinukbarman on 3/6/17.
 */
public class GreaterThanOrEqual extends ComparisonOperators{
    private String split1;
    private String split2;
    private String operand;
    private Table t;
    private Table curr;
    private String condition;

    public GreaterThanOrEqual(String split1, String split2, String operand, Table t, Table curr, String condition) {
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
        System.out.println("Orig : " + t.printTable());
        Parser p = new Parser();
        ArrayList<Integer> removeRowIndices = new ArrayList<>();
        Table newT = p.copyTable(t);

        //Table newT = new Table("tableNew");
        //newT.colMap = t.getLinkedMap();
        Column c = newT.getLinkedMap().get(split1);

        if (condition.equals("Unary")) {
            if (isValidStringOperand(split1, split2, operand, curr)) {
                //means that both column type and split2 are strings.
                for (int i = 0; i < c.getValues().size(); i++) {
                    if (c.getValues().get(i).compareTo(split2) < 0) {
                        removeRowIndices.add(i);
                    }
                }
                System.out.println("Before remove row : " + t.printTable());
                newT.removeRows(removeRowIndices);
                System.out.println("After remove row : " + t.printTable());
                System.out.println("Currtable : " + curr.printTable());
                return newT;
            } else {
                float f = Float.parseFloat(split2);

                for (int i = 0; i < c.getValues().size(); i++) {
                    float colVal = Float.parseFloat(c.getValues().get(i));
                    if (colVal < f) {
                        removeRowIndices.add(i);
                    }
                }

                newT.removeRows(removeRowIndices);

                return newT;
            }
        } else {
            if (isValidStringOperand(split1, split2, operand, curr)) {
                //means that both column type and split2 are strings.
                for (int i = 0; i < c.getValues().size(); i++) {
                    if (c.getValues().get(i).compareTo(split2) < 0) {
                        removeRowIndices.add(i);
                    }
                }

                newT.removeRows(removeRowIndices);

                return newT;
            } else {
                float f = Float.parseFloat(split2);

                for (int i = 0; i < c.getValues().size(); i++) {
                    float colVal = Float.parseFloat(c.getValues().get(i));
                    if (colVal < f) {
                        removeRowIndices.add(i);
                    }
                }

                newT.removeRows(removeRowIndices);

                return newT;
            }
        }



    }

    //return new Table("newT");


}
