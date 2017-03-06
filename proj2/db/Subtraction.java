package db;

/**
 * Created by jhinukbarman on 3/5/17.
 */
public class Subtraction extends ArithmeticOperators {
    private Column a;
    private Column b;

    public Subtraction(Column a, Column b) {
        this.a = a;
        this.b = b;
    }


    @Override
    public Column combineCols() {
        if (a.getMyType().equals("int") && b.getMyType().equals("int")) {
            Column newCol = new Column("c", "int");
            for (int i = 0; i < a.getValues().size(); i++) {
                int a1 = Integer.parseInt(a.getValues().get(i));
                int b1 = Integer.parseInt(b.getValues().get(i));
                String newVal = String.valueOf(a1 - b1);
                newCol.addVal(newVal);
            }
            return newCol;

        } else {
            Column newCol = new Column("c", "float");
            for (int i = 0; i < a.getValues().size(); i++) {
                String s = a.getValues().get(i) + "f";
                float f = Float.parseFloat(s);
                String s1 = b.getValues().get(i) + "f";
                float f1 = Float.parseFloat(s);
                String newVal = String.valueOf(f - f1);
                newCol.addVal(newVal);
            }
            return newCol;
        }


      //  return new Column("NOVALUE", "string"); //shouldn't be returning this ever
    }
}
