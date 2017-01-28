/**
 * Created by jhinukbarman on 1/28/17.
 */
public class LinkedListDeque<Item> {

    private class IntNode {
        public IntNode next;
        public Item item;
        public IntNode prev;

        public IntNode(IntNode p, Item i, IntNode n){
            prev = p;
            item = i;
            next = n;
        }

    }

    private IntNode sentinel;
    private int size;
    private Item temp; //does this work? OH

    public LinkedListDeque(){
        sentinel = new IntNode(null, temp, null);
        size = 0;
    }

    public void addFirst(Item i){
        //sentinel.prev = sentinel;
        sentinel.next = new IntNode(sentinel, i, sentinel.next);
        size += 1;
    }


    public void addLast(Item i){
        size += 1;
        IntNode p = sentinel;
        IntNode last = p.prev;
        last.next = new IntNode(last, i, p);
    }

    public boolean isEmpty(){
        if (sentinel.next == null){
            return true;
        }

        return false;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        IntNode p  = sentinel;
        while(p.next != sentinel){
            System.out.print(p.next.item + " ");
            p = p.next;
        }
    }
/*
    public Item removeFirst(){

    }

    public Item removeLast(){

    }

    public Item get(int index){

    }

    public Item getRecursive(int index){

    }
    */



}
