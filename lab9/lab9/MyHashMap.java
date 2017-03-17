package lab9;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;


/**
 * Created by jhinukbarman on 3/16/17.
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    private int size;
    private Entry<K, V>[] table;
    private int initialCapacity;
    private double loadFactor;
    private HashSet keys = new HashSet();
    //private int threshold;

    private static final int DEFAULT_INITCAPACITY = 16;
    private static final double DEFAULT_LOADFACTOR = 0.75;

    public MyHashMap() {
        this(DEFAULT_INITCAPACITY, DEFAULT_LOADFACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOADFACTOR);

    }
    public MyHashMap(int initialSize, double loadFactor) {
        table = new Entry[initialSize];
        this.initialCapacity = initialSize;
        this.loadFactor = loadFactor;
    }

    private class Entry<K, V> {

        /** Stores KEY as the key in this key-value pair, VAL as the value, and
         *  NEXT as the next node in the linked list. */
        Entry(K k, V v, Entry n) {
            key = k;
            val = v;
            next = n;
            keys.add(key);
        }

        /** Returns the Entry in this linked list of key-value pairs whose key
         *  is equal to KEY, or null if no such Entry exists. */
        Entry get(K k) {
            if (k != null && k.equals(key)) {
                return this;
            }
            if (next == null) {
                return null;
            }
            return next.get(k);
        }

        /** Stores the key of the key-value pair of this node in the list. */
        K key;
        /** Stores the value of the key-value pair of this node in the list. */
        V val;
        /** Stores the next Entry in the linked list. */
        Entry next;

    }


    public void clear() {
        table = (Entry<K, V>[]) new Entry[DEFAULT_INITCAPACITY];
        size = 0;
        initialCapacity = DEFAULT_INITCAPACITY;
       // threshold = (int) (initialCapacity * loadFactor);

    }



    public V get(K key) {
        int hash = key.hashCode() & 0x7fffffff;
        int index = hashCodetoIndex(hash);
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (key.equals(e.key)) {
                return e.val;
            }
        }
        return null;
    }


    public boolean containsKey(K key) {
        return get(key) != null;
    }


    public int size() {
        return size;
    }

    public void resize() {
        size *= 2;
        Entry<K, V>[] newTable = new Entry[size];
        //newtable = new table
        int index = 0;
        Entry newTableEntry = null;
        for (Entry<K, V> e : table) {
            newTableEntry = e;
            while (e != null) {
                newTable[index] = newTableEntry;
                e = e.next;
                newTableEntry.next = e;
                newTableEntry = newTableEntry.next;
            }
        }

        table = newTable;

        /*
        for (int i = 0; i < table.length; i++) {
            newTable.
           // for (Entry<K, V> e : table[i]) {
                newTable.put(key, table[i].get(key));
           // }
        }


        this.m  = temp.m;
        this.n  = temp.n;
        this.st = temp.st;



        loop entry <k,v> e: table
        while e != null
        entry k v next = e.get next
        int hash = e.getkeu hashcod times float
        index find
        e.setnext next table index
        new table index is entry
       */

    }

    public void put(K key, V value) {
        //similar to get
        //arr.add(new Entry(key, value, null));
        //hash key . hashcode
        //int index = hashCodetoIndex(hashCode());
        int hash = key.hashCode() & 0x7fffffff;
        int index = hashCodetoIndex(hash);
        Entry<K, V> newEnt = new Entry(key, value, null); //create a new entry for the key and value

        //for each entry in the table[index[ while e is not null and change e to e.next
            //if new entry equals e then
            //e.setvalue(value)

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (newEnt.equals(e)) {
                e.val = value;
                return;
            }
            if (e.next == null) {
                e.next = newEnt;
            }
        }

        if (size > loadFactor) {
            resize();
        }

        //if size s greater than threshold then resize]]
        size += 1;
    }

    public int hashCodetoIndex(int hashCode) {
        return Math.floorMod(hashCode, table.length);
    }

    public Set<K> keySet() {
        return keys;
    }

    public Iterator<K> iterator() {
        return keys.iterator(); //have a hashset called keys
    }


    //for (K key : map)
    public V remove(K key) {
        throw new UnsupportedOperationException("ERROR");
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException("ERROR");
    }


}
