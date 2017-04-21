import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort {

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] a) {

        //d < (max or min?? of string array a).length
        String[] temp = a;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < a.length; i++) {
            temp[i] = a[i];
            if ((temp[i].length() - 1) < min) {
                min = temp[i].length() - 1;
            }
        }

        for (int d = min; d >= 0; d--) {
            temp = betterCountingSort(temp, d);
            //System.out.println(temp);
        }

        return temp;

    }

    public static String[] betterCountingSort(String[] toSort, int digit) {
        int i = 0;

        String maxString = "";
        int maxValue = Integer.MIN_VALUE;
        for (String j : toSort) {
            if ((int) j.charAt(digit) > maxValue) {
                maxValue = (int) j.charAt(digit);
                maxString = j;
            }
        }

        // gather all the counts for each value
        ArrayList<String>[] counts = new ArrayList[256];

      //  for (ArrayList<String> arr : counts) {
       //     arr = new ArrayList<String>();
      //  }

        for (String s : toSort) {
            //System.out.println("string : " + s);
            //System.out.println("int val : " + (int) s.charAt(digit));
            if (counts[(int) s.charAt(digit)] == null) {
                counts[(int) s.charAt(digit)] = new ArrayList<String>();
            }
            counts[(int) s.charAt(digit)].add(s);
            //System.out.println(counts[(int) s.charAt(digit)]);

        }

        // put the value count times into a new array
        String[] sorted = new String[toSort.length];
        int k = 0;
        for (int x = 0; x < counts.length; x += 1) {
            //for (int j = 0; j < counts[x]; j += 1) {
            if (counts[x] != null) {
                for (String a : counts[x]) {
                    sorted[k] = a;
                    k += 1;
                }
            }
           // }
        }
        return sorted;
        // return the sorted array
    }

        /*
        for (int d = 0; d < temp[0].length(); d++) {
            radixSort(temp, temp.length - 1, d);
        }

        return temp;
        */

        //  }

    /*

    public static void radixSort(String[] unsortedArray, int n, int d){
        ----


        Queue[] queue = new Queue[256];
        for(int k = 0; k < queue.length; k++){
            queue[k] = new PriorityQueue();
        }
        for(int i = d - 1; i >= 0; i--){
            for(int j = 0; j < n; j++){
            }
        }
        for(int index = 0; index < n; index++){
            for(int i = d; i < n; i++){
                char digit = (unsortedArray[index].charAt(i));

                //System.out.println(String.valueOf(Character.toString(digit)));
                //System.out.println(Integer.parseInt("h"));

                int dig = (int) digit;//Integer.parseInt(String.valueOf(digit));
                System.out.println("dig: " + dig);
                System.out.println("index: " + index);
                //queue[dig].add(unsortedArray[index]);
                queue[dig].add(unsortedArray[index]);

            }
        }
        for(int p = 0; p < n; p++){
            unsortedArray[p] = (queue[p].remove()).toString();
        }
        int item = 0;
        for(int queueNum = 0; queueNum < n; queueNum++){
            while(!unsortedArray[queueNum].isEmpty()){
            }
        }
    }


    /*
    private static int getMax(String[] arr) {
        int maxValue = Integer.MIN_VALUE;
        for (String j : arr) {
            if (j > maxValue) {
                maxValue = j;
            }
        }
    }
    */

    private static int getIndex(char letter) {
        return letter - 'a';
    }

    private static void addItem(String[] s, String item) {
        int index = 0;
        while (s[index] != null) {
            index++;
        }
        s[index] = item;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     * destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String at start)
     * @param end    int for where to end sorting in this method (does not include String at end)
     * @param index  the index of the character the method is currently sorting on
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index) {
        //TODO use if you want to
        /*

        public static void LSDRadixSort(int[] arr) {
        for (int d = 0; d < numDigitsInAnInteger; d++) {
            stableSortOnDigit(arr, d);
        }
        */

    }
}


