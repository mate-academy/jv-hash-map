package core.basesyntax;

import java.util.HashMap;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR =  0.75f;
    private Node<K,V>[] bucket;
    private int capacity;
    private float loadFactor;
    private int size;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        loadFactor = DEFAULT_LOAD_FACTOR;
        bucket = new Node[this.capacity];
        size = 0;
    }

    public MyHashMap(int capacity) {
        this.capacity = capacity;
        bucket = new Node[this.capacity];
        size = 0;
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        bucket = new Node[this.capacity];
        size = 0;
    }


    @Override
    public void put(K key, V value) {

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    private static class Node<K, V> {
        K key;
        V value;
        public Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
