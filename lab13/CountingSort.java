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
        int i = 0;

        int maxValue = Integer.MIN_VALUE;
        for (int j : toSort) {
            if (j > maxValue) {
                maxValue = j;
            }
        }


        int[] b = new int[toSort.length];
        int[] copyofa = new int[toSort.length];
        int min = 0;
        for (i = 0; i < toSort.length; i++) {
            b[i] = 0;
            if (min > toSort[i]) {
                min = toSort[i];
            }
        }

        int newMaxValue = maxValue - min;
        for (i = 0; i < toSort.length; i++) {
            copyofa[i] = toSort[i] - min;
        }

        System.out.println("array size: " + Math.abs(newMaxValue) + 1);
        int[] temp = new int[Math.abs(newMaxValue) + 1];

        for (i = 0; i <= newMaxValue; i++) {
            temp[i] = 0;
        }

        for (i = 0; i < toSort.length; i++) {
            temp[copyofa[i]] += 1;
        }

        for (i = 1; i <= newMaxValue; i++) {
            temp[i] = temp[i] + temp[i - 1];
        }

        for (i = toSort.length - 1; i >= 0; i--) {
            b[temp[copyofa[i]] - 1] = copyofa[i];
            temp[copyofa[i]] = temp[copyofa[i]] - 1;
        }

        for (i = 0; i < toSort.length; i++) {
            b[i] = b[i] + min;
        }

        return temp;
    }
}
