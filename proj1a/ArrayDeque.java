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
    private int usageRatio;
    private int RFACTOR = 2;

    /**
     * The constructor creates an instance of the ArrayDeque class with the empty
     * of length 8, size, nextFirst, nextLast and usageRatio.
     */
    public ArrayDeque(){
        items = (Item[]) new Object[8];
        size = 0;
        nextFirst = 2;
        nextLast = 3;
        usageRatio = 0;
    }

    /**
     * This method adds an item to the beginning of the array, and decrements nextFirst.
     * @param i item added to the array
     */
    public void addFirst(Item i){
        if (nextLast == nextFirst){
            rearrangeList();
        }
        items[nextFirst] = i;
        if (nextFirst == 0){
            nextFirst = items.length;
        }
        nextFirst--;
        size++;
    }

    private void resize(int srcPos, int capacity, int destPos){
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, a, destPos, size);
        items = a;
    }

    private boolean ifHalved(){ //of half it in here
        usageRatio = size/items.length;
        //size of ARRAY, not list is halved
        if (usageRatio < 0.25){
            return true;
            //call resize method
        }
        return false;
    }


    private void rearrangeList(){
            Item[] a = (Item[]) new Object[size*RFACTOR];
            System.arraycopy(items, nextFirst+1, a, 0, items.length-(nextFirst+1));
            System.arraycopy(items, 0, a, items.length-(nextFirst+1), nextFirst);
            items = a;
            nextFirst = items.length-1;
            nextLast = size;  //because nextLast gets incremented later in the method
    }

    /**
     * This method removes an item from the end of the array, and increments nextLast.
     * @param i item to be removed from array
     */
    public void addLast(Item i){
        if (nextLast == nextFirst){
            rearrangeList();
        }
        items[nextLast] = i;
        if (nextLast < items.length - 1) {
            nextLast++;
        }
        else {
            nextLast = 0;
        }
        size++;
    }


    /**
     * This method checks if the array is empty by checking if size is 0.
     * @return true if empty, else false
     */
    public boolean isEmpty(){
        if (size == 0){
            return true;
        }

        return false;
    }

    /**
     * This method returns the size of the array.
     * @return size
     */
    public int size(){
        return size;
    }

    public void printDeque() {
        for(int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
    }

    /**
     * This method removes the first item of the array and changes the nextFirst position.
     * @return first item
     */
    public Item removeFirst(){
       int nextElement = nextFirst + 1;
       if (nextElement>=items.length){
           nextElement = 0;
       }
       if (items[nextElement] == null){
            return null;
       }
        Item first = items[nextElement];
        items[nextElement] = null; //maybe not necessary
        nextFirst = nextElement;
        size = size - 1;
        if (nextLast == nextFirst){
            rearrangeList();
        }
        return first;

    }

    /**
     * This method removes the last item of the array and changes the nextLast position.
     * @return last item
     */
    public Item removeLast(){
        int lastElement = nextLast - 1;
        if (lastElement < 0){
            lastElement = items.length-1;
        }
        if (items[lastElement] == null){
            return null;
        }
        Item last = items[lastElement];
        items[lastElement] = null; //maybe not necessary
        nextLast = lastElement;
        size = size - 1;
        if (nextLast == nextFirst){
            rearrangeList();
        }
        return last;
    }

    /**
     * This method finds the item in the at the position of the index in
     * the circular array and returns it.
     * @param index position of the circular array
     * @return item at the index
     */
    public Item get(int index){
        if (index >= size){
            return null;
        }
        int newIndex = nextFirst + 1 + index;
        if (newIndex > items.length-1){
            newIndex = newIndex - items.length;
        }
        return items[newIndex];
    }




}
