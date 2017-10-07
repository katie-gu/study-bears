package lab8;

//import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
//import java.util.

/**
 * Created by jhinukbarman on 3/9/17.
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    /** Removes all of the mappings from this map. */
   // HashMap<K,V> bMap = new HashMap<K, V>();
    private Node root;
    private int size;          // number of nodes in subtree

    private class Node {
        private K key;           // sorted by key
        private V val;         // associated data
        private Node left, right;  // left and right subtrees
        //private int size;          // number of nodes in subtree

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
            //this.size = size;
        }
    }

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (key == null) { //may not need this if statement
            return false;
            //throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }


    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        return getHelper(root, key);
    }

    //necessary/important method
    private V getHelper(Node n, K key) {
        if (n == null) {
            return null;
        }
        int comp = key.compareTo(n.key);
        if (comp < 0) {
            return getHelper(n.left, key);
        }
        else if (comp > 0) {
            return getHelper(n.right, key);
        }
        else { //comp == 0 --> we found the key
            return n.val;
        }
    }


    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        /*
        if (root == null) {
            put(key, value, root);
        } else {
            put(key, value, new Node(key, value, size()));

        }
        */
        //might not need these if statements
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        if (value == null) {
            //delete(key);
            return;
        }
        root = put(root, key, value);
        //assert check();
    }



    private Node put(Node n, K key, V value) {
        /*
        if (root == null) {
            root.key = key;
            root.val = value;
        } else {
            if (key < n.key) {
                n.left = new Node(key, value, size());
            }
            //n.key = key;
           // n.val = value;
           // n.right = n.
        }
        */

        if (n == null) {
            size += 1;
            return new Node(key, value);
        }
        int cmp = key.compareTo(n.key);
        if  (cmp < 0) {
            n.left  = put(n.left,  key, value);
            //n.left.parent = n;
        }
        else if (cmp > 0) {
            n.right = put(n.right, key, value);
            //n.right.parent = n;
        }
        else { //cmp == 0
            n.val = value;
        }

        //n.size = 1 + size(n.left) + size(n.right);
        return n;



    }

    public void printInOrder() {
        printInOrderHelper(root);
    }

    private void printInOrderHelper(Node p) {
        if (p == null) {
            return;
        }
        printInOrderHelper(p.left);
        System.out.println(" " + p.toString() + " ");
        printInOrderHelper(p.right);
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }


    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }


}
