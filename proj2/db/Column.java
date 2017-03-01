package db;

import java.math.BigDecimal;
import java.util.*;
import java.math.RoundingMode;
/**
 * Created by jhinukbarman on 2/25/17.
 */
public class Column{
    public ArrayList<String> myValues;
    public String myName;
    public String myType;


    public Column(String name, String type) {
        myName = name;
        myType = type;
        myValues = new ArrayList<String>();
    }

    public void addVal(String val) {
        myValues.add(val);
    }

    public void printColVal(int index) {
        String curr = myValues.get(index);

        if (myType.equals("string")){
            System.out.print("'" + curr + "'");
        } else if (myType.equals("float")){
            float f = Float.parseFloat(curr);
            System.out.println(BigDecimal.valueOf(f).setScale(3, RoundingMode.CEILING));
        } else {
            System.out.print(curr);
        }
    }

    public void printColHead() {
        System.out.print(myName + " " + myType);
    }

    public ArrayList<String> getValues() {
        return myValues;
    }


}
