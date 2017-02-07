import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by jhinukbarman on 2/4/17.
 */
public class TestArrayDeque1B {
    /**
     * This method randomly generates numbers and compares between the StudentArrayDeque
     * and ArrayDequeSolution results.
     * This code was borrowed from StudentArrayDequeLauncher.
     */
    @Test
    public static void randomTest(){
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();

        for (int i = 0; i < 10; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                sad1.addLast(i);
                ads1.addLast(i);

            } else {
                sad1.addFirst(i);
                ads1.addFirst(i);
            }
        }
        System.out.println("hi");

        if ((!sad1.isEmpty()) && (!ads1.isEmpty())) {
            Integer x = sad1.removeFirst();
            Integer y = ads1.removeFirst();
            //assertEquals(x, y);

        }

        sad1.printDeque();
        ads1.printDeque();
    }

    public static void main(String[] args) {
        System.out.println("hellloo");
       // randomTest();
        /* Helpful challenge: Modify this file so that it outputs the list of
           operations as a String. Use the OperationSequence class. */
    }

}
