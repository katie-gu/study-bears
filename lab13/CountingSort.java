import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 * 
 * @author 	Akhil Batra
 * @version	1.1 - April 16, 2016
 * 
**/
public class CountingSort {
    
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     *  does not touch original array (non-destructive method)
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
    **/
    public static int[] naiveCountingSort(int[] arr) {
        //TODO make it work with arrays containing negative numbers.
        //find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i] += 1;
        }

        // put the value count times into a new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // return the sorted array
        return sorted;





    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     *  does not touch original array (non-destructive method)
     * 
     * @param toSort int array that will be sorted
    **/
    public static int[] betterCountingSort(int[] toSort) {

        System.out.print("toSort: ");
        for (int i : toSort) {
            System.out.print(i + " ");
        }
        System.out.println();

        ArrayList<Integer> numNeg = new ArrayList<>();
        TreeSet<Integer> uniqueNeg = new TreeSet<>();
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : toSort) {
            //System.out.println("hi");
            if (i < 0) {
                numNeg.add(i);
                uniqueNeg.add(i);
            }
            if (i > max) {
                max = i;
            }
        }

        // gather all the counts for each value
        int negIndex = 0;
        int addNegSize = 0;

        /*
        if (uniqueNeg.size() != 0) {
            addNegSize = Math.abs(uniqueNeg.size());
        }
        */

        System.out.println("counts size : " + Math.abs(max) + 2 + numNeg.size());
        int[] counts = new int[Math.abs(max) + 2 + numNeg.size()];
        for (int i : toSort) {
            //System.out.println("ok then");
            if (i < 0) {
                negIndex = max + Math.abs(i);
                counts[negIndex] += 1;
            } else {
                counts[i] += 1;
            }

        }

        // put the value count times into a new array

        int[] sorted = new int[toSort.length - numNeg.size()];
        int[] negSorted = new int[numNeg.size()];

        int k = 0;
        for (int i = 0; i < sorted.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                System.out.println("in here RN");
                sorted[k] = i;
            }
        }

        k = 0;
        for (int i = max + 1; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
              //  System.out.println("counts: " + counts[i]);
                System.out.println("in here");
                negSorted[k] = 0 - (i - max);
            }
        }

        //List<Object> negList = Arrays.asList(negSorted);
        //Collections.reverse(negList);


        for(int i = 0; i < negSorted.length / 2; i++) {
            int temp = negSorted[i];
            negSorted[i] = negSorted[negSorted.length - i - 1];
            negSorted[negSorted.length - i - 1] = temp;
        }
        //negList.toArray();
        /*
        int[] negOrdered = new int[negList.size()];
        for (int i = 0; i < negOrdered.length; i++) {
            negOrdered[i] = negList.get(i);
        }
        */

        /*
        System.out.print("Sorted: ");
        for (int i : sorted) {
            System.out.print(i + " ");
        }

        System.out.println("\n Neglist : ");

        for (Object i : negSorted) {
            System.out.print(i + " ");
        }
        */

       // System.out.println("sorted: " + sorted.toString());
      //  System.out.println("neglist " + negList.toString());

        int [] newArray = new int[sorted.length + negSorted.length];
        System.arraycopy(negSorted, 0, newArray, 0, negSorted.length);
        System.arraycopy(sorted, 0, newArray, negSorted.length, sorted.length );



        System.out.print("result : ");
        for (int i : newArray) {
            System.out.print(i + " ");
        }

        // return the sorted array

        return newArray;

    }
}
