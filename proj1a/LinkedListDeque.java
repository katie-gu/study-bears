/**
 * Created by jhinukbarman on 1/28/17.
 * This class creates a Doubly Linked List of a generic type "Item." The list has a sentinel
 * node to act as a middle man and points to the first and last elements of the list.
 * This class has the variables sentinel and size.
 */
public class LinkedListDeque<Item> {

    /*
     * The Node class is only access by LinkedListDeque class. An Node instance has the
     * variables next, item and prev.
     */
    private class Node {
        public Node next;
        public Item item;
        public Node prev;

        /**
         * The constructor creates an instance of the node class with the given
         * prev, item and next.
         * @param p pointer to previous node
         * @param i item in current node
         * @param n pointer to next node
         */
        public Node(Node p, Item i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Node sentinel;
    private int size;
    private Item temp;

    /**
     * This constructor creates an empty instance of LinkedListDeque.
     */
    public LinkedListDeque() {
        sentinel = new Node(null, temp, null);
        size = 0;
    }

    /**
     * This method adds the given item to the beginning of the list.
     * @param i item to be added to beginning
     */
    public void addFirst(Item i) {
        sentinel.next = new Node(sentinel, i, sentinel.next);
        if(sentinel.next.next != null) {
            sentinel.next.next.prev = sentinel.next;
        }
        else {
            sentinel.prev = sentinel.next;
            sentinel.next.next = sentinel;
        }

        sentinel.prev.next = sentinel;
        size += 1;
    }

    /**
     * This method adds the given item to the end of the list.
     * @param i item to be added to end
     */
    public void addLast(Item i) {
        if (sentinel.prev == null) {
            sentinel.prev = new Node(sentinel.prev, i, sentinel.next);
            sentinel.next = sentinel.prev;
            sentinel.prev.prev = sentinel.prev;
            sentinel.prev.next = sentinel.next;
        }
        else {
            sentinel.prev.next = new Node(sentinel.prev, i, sentinel);
            sentinel.prev = sentinel.prev.next;
        }
        size += 1;
    }

    /**
     * This method returns the boolean true if the list is empty
     * or else false.
     * @return boolean false or true
     */
    public boolean isEmpty() {
        if (sentinel.next == null || sentinel.next == sentinel){
            return true;
        }

        return false;
    }

    /**
     * This method returns the size of the list.
     * @return size
     */
    public int size(){
        return size;
    }

    /**
     * This method prints the items of the list with spaces in between them.
     */
    public void printDeque() {
        Node p  = sentinel;
        while(p.next != sentinel){
            System.out.print(p.next.item + " ");
            p = p.next;
        }
    }

    /**
     * This method removes the first item from the list and returns the item.
     * @return removed item
     */
    public Item removeFirst() {
        if (!isEmpty()) {
            Item removed = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return removed;
        }
            return null;
    }

    /**
     * This method removes the last item from the list and returns that item.
     * @return removed item
     */
    public Item removeLast() {
        if(!isEmpty()) {
            Item removed = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return removed;
        }
            return null;
    }

    /**
     * This method finds the item in the position of the index iteratively
     * and returns the item.
     * @param index
     * @return item at position of the index
     */
    public Item get(int index) {
        Node p = sentinel;
        while (index >= 0){
            p = p.next;
            if (p.next == sentinel) {
                return null;
            }
            index = index - 1;
        }

        return p.item;
    }

    /**
     * This method finds the item of the index recursively and returns the item.
     * @param index
     * @return items at position of the index
     */
    public Item getRecursive(int index) {
        //Node p = getNode(sentinel);
        if (index >= size){
            return null;
        }
        return getNode(sentinel, index).item;
    }

    /**
     * This is a helper method for the getRecursive method. It
     * gets the node at a particular index.
     * @param p current node
     * @param index position of the desired item
     * @return recursive call to getNode
     */
    private Node getNode(Node p, int index) {
        if (index == 0) {
            return p.next;
        }
        return getNode(p.next, index-1);
    }

}
