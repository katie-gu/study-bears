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

    public String printColVal(int index) {
        String curr = myValues.get(index);
        String temp = curr;


        if (myType.equals("string")){
            temp = temp.replace("\'\'", "\'");
            return temp;
        } else if (myType.equals("float")){
            if (curr == "NOVALUE") {
                return curr;
            }
            float f = Float.parseFloat(curr);
            return (BigDecimal.valueOf(f).setScale(3, RoundingMode.DOWN).toString());
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
