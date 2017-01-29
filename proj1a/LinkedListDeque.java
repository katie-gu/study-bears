/**
 * Created by jhinukbarman on 1/28/17.
 */
public class LinkedListDeque<Item> {

    private class Node {
        public Node next;
        public Item item;
        public Node prev;

        public Node(Node p, Item i, Node n){
            prev = p;
            item = i;
            next = n;
        }
    }

    private Node sentinel;
    private int size;
    private Item temp; //does this work? OH
    //private Node lastNode; //does this work? for last method

    public LinkedListDeque(){
        sentinel = new Node(null, temp, null);
        size = 0;
    }

    public void addFirst(Item i){
        //sentinel.prev = sentinel;
        //IntNode p = sentinel;
        sentinel.next = new Node(sentinel, i, sentinel.next);
        //sentinel.prev = sentinel.next;
        if(sentinel.next.next != null) {
            sentinel.next.next.prev = sentinel.next;
        }
        else{
            sentinel.prev = sentinel.next;
            sentinel.next.next = sentinel;
        }
       // sentinel.prev = sentinel.next.next;
        //nullpointer exception
        size += 1;
    }


    public void addLast(Item i){
        size += 1;
        Node p = sentinel;
        Node last = p.prev;
        last.next = new Node(last, i, p);
        p.prev = last.next;
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
        Node p  = sentinel;
        while(p.next != sentinel){
            System.out.print(p.next.item + " ");
            p = p.next;
        }
    }

    public Item removeFirst(){
        if (!this.isEmpty()) {
            Item removed = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            return removed;
        }
            return null;
    }

    public Item removeLast(){
        Item removed = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return removed;
    }

    public Item get(int index){
        Node p = sentinel;
        while (index>=0){
            p = p.next;
            if (p.next == sentinel){
                return null;
            }
            index = index - 1;
        }

        return p.item;
    }

    public Item getRecursive(int index){
        //Node p = getNode(sentinel);
        if (index>=size){
            return null;
        }
        return getNode(sentinel, index).item;
    }

    //ask in OH
    private Node getNode(Node p, int index){
        if (index == 0){
            return p.next;
        }
        return getNode(p.next, index-1);
    }

}
