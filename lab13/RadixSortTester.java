import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jhinukbarman on 4/20/17.
 */
public class RadixSortTester {

    String[] testArr = {"hello", "success!", "hahahaha", "iloveyou", "heythisdidntwork", "why", "tellme", "123", "!!"};
    String[] expect = {"!!", "123", "hahahaha", "hello", "heythisdidntwork", "iloveyou", "success!", "tellme", "why"};

    /*
    public static void assertIsSorted(String[] a) {
        int previous = Integer.MIN_VALUE;
        for (String x : a) {
            assertTrue(x >= previous);
            previous = x;
        }
    }
    */

    @Test
    public void testNaiveWithNonNegative() {
        String[] radixSort = RadixSort.sort(testArr);
        //assertEquals(, 3);
        //assertIsSorted(testArr);
        System.out.print("actual: ");
        for (String s : radixSort) {
            System.out.print(s + " ");
        }

        System.out.println();
        System.out.print("expected: ");
        for (String s : expect) {
            System.out.print(s + " ");
        }

        //assertEquals(testArr, expect);

    }
}
