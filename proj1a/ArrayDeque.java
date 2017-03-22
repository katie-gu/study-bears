/**
 * Created by jhinukbarman on 1/29/17.
 * This class creates a circular ArrayList with nextFirst and nextLast as pointers
 * to determine the start and end of the array. The RFACTOR is used to maximize efficiency
 * when resizing the array and usageRatio determines when the array should be halved.
 */
public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private double usageRatio;
    private int RFACTOR = 2;

    /**
     * The constructor creates an instance of the ArrayDeque class with the empty
     * of length 8, size, nextFirst, nextLast and usageRatio.
     */
    public ArrayDeque() {
        items = (Item[]) new Object[8]; //make length 8 AGAIN
        size = 0;
        nextFirst = 2;
        nextLast = 3;
        usageRatio = 0;
    }

    /**
     * This method adds an item to the beginning of the array, and decrements nextFirst.
     * @param i item added to the array
     */
    public void addFirst(Item i) {
        if (nextLast == nextFirst) {
            resize();
        }
        items[nextFirst] = i;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst--;
        }
        size++;
    }

    /**
     * This method checks if the length of the stored array is is greater and 16 and the
     * usageRatio is less than .25. If this is true, it decreases the size of the stored array
     * as 3 times the size of the array.
     */
    private void checkUsageRatio() {
        usageRatio = size / items.length;
        if ((items.length >= 16) && (usageRatio < 0.25)) {
            Item[] a = (Item[]) new Object[size * 3];

            if (nextLast > nextFirst) {
                System.arraycopy(items, nextFirst + 1, a, 0, (nextLast - nextFirst) - 1);
            } else {
                if (nextFirst < items.length - 1) {
                    System.arraycopy(items, nextFirst + 1, a, 0, items.length - (nextFirst + 1));
                }
                System.arraycopy(items, 0, a, items.length - (nextFirst + 1), nextLast);

            }
            items = a;
            nextFirst = items.length - 1;
            nextLast = 0;
        }
    }

    /**
     * Resize changes the size of the array as size times RFACTOR and rearranges
     * the list if nextFirst and nextLast point in the same position.
     */
    private void resize() {
        Item[] a = (Item[]) new Object[size * RFACTOR];
        System.arraycopy(items, nextFirst + 1, a, 0, items.length - (nextFirst + 1));
        System.arraycopy(items, 0, a, items.length - (nextFirst + 1), nextFirst);
        items = a;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    /**
     * This method removes an item from the end of the array, and increments nextLast.
     * @param i item to be removed from array
     */
    public void addLast(Item i) {
        if (nextLast == nextFirst) {
            resize();
        }
        items[nextLast] = i;
        if (nextLast < items.length - 1) {
            nextLast++;
        } else {
            nextLast = 0;
        }
        size++;
    }

    /**
     * This method checks if the array is empty by checking if size is 0.
     * @return true if empty, else false
     */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }

        return false;
    }

    /**
     * This method returns the size of the array.
     * @return size
     */
    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
    }

    /**
     * This method removes the first item of the array and changes the nextFirst position.
     * @return first item
     */
    public Item removeFirst() {
        int nextElement = nextFirst + 1;
        if (nextElement >= items.length) {
            nextElement = 0;
        }
        if (items[nextElement] == null) {
            return null;
        }
        Item first = items[nextElement];
        items[nextElement] = null;
        nextFirst = nextElement;
        size = size - 1;
        if (nextLast == nextFirst) {
            resize();
        }
        if (size != 0) {
            checkUsageRatio();
        }
        return first;
    }

    /**
     * This method removes the last item of the array and changes the nextLast position.
     * @return last item
     */
    public Item removeLast() {
        int lastElement = nextLast - 1;
        if (lastElement < 0) {
            lastElement = items.length - 1;
        }
        if (items[lastElement] == null) {
            return null;
        }
        Item last = items[lastElement];
        items[lastElement] = null; //maybe not necessary
        nextLast = lastElement;
        size = size - 1;
        if (nextLast == nextFirst) {
            resize();
        }
        if (size != 0) {
            checkUsageRatio();
        }
        return last;
    }

    /**
     * This method finds the item in the at the position of the index in
     * the circular array and returns it.
     * @param index position of the circular array
     * @return item at the index
     */
    public Item get(int index) {
        if (index >= size) {
            return null;
        }
        int newIndex = nextFirst + 1 + index;
        if (newIndex > items.length - 1) {
            newIndex = newIndex - items.length;
        }

        return items[newIndex];
    }

}
