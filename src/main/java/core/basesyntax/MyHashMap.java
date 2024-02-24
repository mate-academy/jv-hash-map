package core.basesyntax;

import java.security.PrivateKey;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int COEFFICIENT_OF_EXPANSION = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int currentCapacity;
    private int size;
    private K[] keys;
    private V[] values;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        keys = (K[]) new Object[DEFAULT_INITIAL_CAPACITY];
        values = (V[]) new Object[DEFAULT_INITIAL_CAPACITY];
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> current = new Node<>(key,value);
        current.hash = Objects.hash(key);
        int index = current.hash % currentCapacity;
        if (Objects.equals(key,keys[index])) {
            values[index] = value;

        }
        size++;
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeeded() {
        int sizeForResize = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
        if (size >= sizeForResize) {

        }
    }

    private int hash(K key) {
        return 0;
    }


    static class Node<K,V> {
        private int hash;
        private K key;

        private V value;

        private Node<K,V> next;
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}
