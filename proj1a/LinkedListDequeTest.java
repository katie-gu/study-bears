/** Performs some basic linked list tests. */
public class LinkedListDequeTest {



	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out empty checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed. 
	 * The \n means newline. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	/** Adds a few things to the list, checking isEmpty() and size() are correct, 
	  * finally printing the results. 
	  *
	  * && is the "and" operation. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");
		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
		/*
		LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst("front");
		
		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(1, lld1.size()) && passed;
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.addLast("middle");
		passed = checkSize(2, lld1.size()) && passed;

		lld1.addLast("back");
		passed = checkSize(3, lld1.size()) && passed;

		System.out.println("Printing out deque: ");
		lld1.printDeque();

		printTestStatus(passed);
		*/
	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
		//try with String as Generic type!!
		//LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		ArrayDeque<Integer> lld2 = new ArrayDeque<Integer>();
		//should be empty
		//boolean passed = checkEmpty(true, lld1.isEmpty());


        //Checks if the nextFirst and nextLast are same
        lld2.addFirst(8);
        lld2.addLast(9);
        lld2.addLast(10);
        lld2.addLast(11);
        lld2.addLast(12);
        lld2.addLast(13);
        lld2.addLast(14);
        lld2.addLast(15);
        lld2.addLast(16);
        lld2.addFirst(7);
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeLast();
        lld2.removeLast();
        lld2.removeLast();
        System.out.println(lld2.get(2));
       // lld2.removeLast();
       // lld2.removeLast();
      //  lld2.removeLast();
      //  lld2.removeLast();
      //  lld2.removeLast();





        //Also checks if the nextFirst and nextLast are same
        //with nextFirst = 2 and nextLast = 3;
        /*
        lld2.addLast(4);
        lld2.addLast(5);
        lld2.addLast(6);
        lld2.addLast(7);
        lld2.addLast(8);
        lld2.addLast(9);
        lld2.addLast(10);
        lld2.addLast(11);
        lld2.addLast(12);
        lld2.addFirst(35);
        */
    /*
        lld2.addFirst(35);
        lld2.addFirst(36);
        lld2.addFirst(37);
        lld2.addFirst(38);
        lld2.addFirst(39);
        lld2.addFirst(40);
        lld2.addFirst(41);
        lld2.addFirst(42);
        lld2.addFirst(43);
        lld2.addFirst(44);

*/


        //lld1.addFirst(8);
		//lld1.addLast(11);
		//lld1.getRecursive(1);
		//lld1.printDeque();
		//lld1.removeFirst();
		//int theSize = lld1.size();


		// should not be empty 
		//passed = checkEmpty(false, lld1.isEmpty()) && passed;


		//lld1.removeFirst();
		// should be empty
		//passed = checkEmpty(true, lld1.isEmpty()) && passed;

		//printTestStatus(passed);

	}


	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();

		//LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		//lld1.addFirst(10);


	}
} 