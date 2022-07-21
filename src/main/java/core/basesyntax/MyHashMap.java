package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private float loadFactor;
    LinkedList<Entry<K, V>>[] table;

    public MyHashMap(){
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity){
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor){
        if (initialCapacity > MAXIMUM_CAPACITY){
            this.threshold = MAXIMUM_CAPACITY;
        }else{
            this.threshold = trimToPowerOf2(initialCapacity);
        }

        this.loadFactor = loadFactor;
        table = new LinkedList[threshold];
    }




    @Override
    public void put(K key, V value) {
        return ;

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int trimToPowerOf2(int initialCapacity) {
        int capacity = 1;
        while (capacity < initialCapacity){
            capacity <<= 1;
        }
        return capacity;
    }
}
