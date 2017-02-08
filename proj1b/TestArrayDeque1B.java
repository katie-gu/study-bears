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
    public void randomTest() {
        OperationSequence fs = new OperationSequence();
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();

        for (int i = 0; i < 1000; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                DequeOperation dequeOp1 = new DequeOperation("addFirst", i);
                fs.addOperation(dequeOp1);
                sad1.addLast(i);
                ads1.addLast(i);
            } else {
                if ((!sad1.isEmpty()) && (!ads1.isEmpty())) {
                    DequeOperation dequeOp3 = new DequeOperation("removeFirst");
                    fs.addOperation(dequeOp3);
                    Integer actual = sad1.removeFirst();
                    Integer expected = ads1.removeFirst();
                    assertEquals(fs.toString(), expected, actual);
                }

                if ((!sad1.isEmpty()) && (!ads1.isEmpty())) {
                    DequeOperation dequeOp4 = new DequeOperation("removeLast");
                    fs.addOperation(dequeOp4);
                    Integer actual = sad1.removeLast();
                    Integer expected = ads1.removeLast();
                    assertEquals(fs.toString(), expected, actual);
                }

                DequeOperation dequeOp2 = new DequeOperation("addLast", i);
                fs.addOperation(dequeOp2);
                sad1.addFirst(i);
                ads1.addFirst(i);

            }
        }
    }

    public static void main(String[] args) {
        TestArrayDeque1B test1 = new TestArrayDeque1B();
        test1.randomTest();
        /* Helpful challenge: Modify this file so that it outputs the list of
           operations as a String. Use the OperationSequence class. */
    }

}
