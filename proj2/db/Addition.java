package db;

/**
 * Created by jhinukbarman on 3/5/17.
 */
public class Addition extends ArithmeticOperators {
    private Column a;
    private Column b;
    private String name;
    public Addition(Column a, Column b, String name) {
        this.a = a;
        this.b = b;
        this.name = name;
    }

    @Override
    public Column combineCols() {
        if (checkValid(a, b)) {
            if (a.getMyType().equals("string")) {
                Column newCol = new Column(name, "string");
                for (int i = 0; i < a.getValues().size(); i++) {
                    newCol.addVal(a.getValues().get(i) + b.getValues().get(i));
                }
                return newCol;
            } else if (a.getMyType().equals("int") && b.getMyType().equals("int")) {
                Column newCol = new Column(name, "int");
                for (int i = 0; i < a.getValues().size(); i++) {
                    int a1 = Integer.parseInt(a.getValues().get(i));
                    int b1 = Integer.parseInt(b.getValues().get(i));
                    String newVal = String.valueOf(a1 + b1);
                    newCol.addVal(newVal);
                }
                return newCol;
            /*}  else if (a.getMyType().equals("float") && b.getMyType().equals("float")) {
                Column newCol = new Column("c", "float");
                for (int i = 0; i < a.getValues().size(); i++) {
                    //convert
                    newCol.addVal(a.getValues().get(i) + b.getValues().get(i));
                }
                return newCol;
                */
            } else {

            //if ((a.getMyType().equals("int") && b.getMyType().equals("float")) ||
               // (a.getMyType().equals("float") && b.getMyType().equals("int"))) {
                Column newCol = new Column(name, "float");
                for (int i = 0; i < a.getValues().size(); i++) {
                    String s1 = a.getValues().get(i) + "f";
                    float f = Float.parseFloat(s1);
                    String s2 = b.getValues().get(i) + "f";
                    float f1 = Float.parseFloat(s2);
                    String newVal = String.valueOf(f + f1);
                    newCol.addVal(newVal);
                }
                return newCol;
            }

        }
       // for ()
        return new Column("NONAME", "string"); //replace this line
    }
}
