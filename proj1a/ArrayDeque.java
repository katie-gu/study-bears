/**
 * Created by jhinukbarman on 1/29/17.
 */
public class ArrayDeque<Item> {

    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private int usageRatio;

    public ArrayDeque(){
        items = (Item[]) new Object[8];
        size = 0;
    }

    public void addFirst(Item i){
        //sentinel.first = i;
        size++;
    }

    private void resize(int capacity){
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }
    public void addLast(Item i){
        if (size == items.length){
            resize(size + 1);
        }
        items[size] = i;
        size++;
    }

    public boolean isEmpty(){

    }

    public int size(){
        return size;
    }

    public void printDeque(){

    }

    public Item removeFirst(){

    }

    public Item removeLast(){

    }

    public Item get(int index){

    }


}
