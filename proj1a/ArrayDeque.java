/**
 * Created by jhinukbarman on 1/29/17.
 */
public class ArrayDeque<Item> {

    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private int usageRatio;
    private int RFACTOR = 2;

    public ArrayDeque(){
        items = (Item[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
        usageRatio = 0;
    }

    public void addFirst(Item i){
        ifFullList();
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

    private boolean ifHalved(){
        usageRatio = size/items.length;
        //size of ARRAY, not list is halved
        if (usageRatio < 0.25){
            //call resize method
        }
    }


    private void ifFullList(){
        if (nextLast == nextFirst){
            Item[] a = (Item[]) new Object[size*RFACTOR];
            System.arraycopy(items, nextFirst+1, a, 0, items.length-(nextFirst+1));
            System.arraycopy(items, 0, a, items.length-(nextFirst+1), nextFirst);
            items = a;
            nextFirst = items.length-1;
            nextLast = size;  //because nextLast gets incremented later in the method
        }
    }

    public void addLast(Item i){
        ifFullList();
        items[nextLast] = i;

        if (nextLast < items.length - 1) {
            nextLast++;
        }
        else {
            nextLast = 0;
        }
        size++;
    }


    public boolean isEmpty(){
        if (size == 0){
            return true;
        }

        return false;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        //int i = nextFirst + 1;
        for(int i = nextFirst + 1; i<size; i++) {
            if (items[i] != null) {
                System.out.print(items[i] + " ");
            }
        }


    }

    public Item removeFirst(){
        if (items[nextFirst+1] == null){
            return null;
        }
        Item first = items[nextFirst+1];
        items[nextFirst] = null;
        return first;

    }
/*
    public Item removeLast(){
        Item last = items[size-1];
        items[size - 1] = null;
        size = size - 1;
        return last;
    }

    public Item get(int index){
        if (index < size) {
            return items[index];
        }
        else{
            return null;
        }
    }
    */



}
