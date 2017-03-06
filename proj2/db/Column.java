package db;

//import java.math.BigDecimal;
import java.util.ArrayList;
//import java.math.RoundingMode;
/**
 * Created by jhinukbarman on 2/25/17.
 */
public class Column {
    private ArrayList<String> myValues;
    private String myName;
    private String myType;


    public Column(String name, String type) {
        myName = name;
        myType = type;
        myValues = new ArrayList<String>();
    }

    public void addVal(String val) {

        myValues.add(val);
    }

    public String printColVal(int index) {
        String curr = myValues.get(index);
        String temp = curr;


        if (myType.equals("string")) {
            temp = temp.replace("\'\'", "\'");
            return temp;
        } else if (myType.equals("float")) {
            if (curr.equals("NOVALUE")) {
                return curr;
            }
            float f = Float.parseFloat(curr);
            String stringFloat = String.format("%.3f", f);
            return stringFloat;
            //return (BigDecimal.valueOf(f).setScale(3, RoundingMode.HALF_UP).toString());
            //System.out.println(BigDecimal.valueOf(f).setScale(3, RoundingMode.CEILING));
        } else {
            return curr;
        }
    }


    public String printColHead() {
        return myName + " " + myType;
    }

    public ArrayList<String> getValues() {
        return myValues;
    }

    public String getName() {
        return myName;
    }

    public String getMyType() {
        return myType;
    }



}
