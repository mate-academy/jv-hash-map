package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = hash(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node node = table[hash];
            while (node.next != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (Objects.equals(key, node.key)) {
                node.value = value;
            } else {
                node.next = new Node<>(hash, key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node node = table[hash];
        if (node != null) {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(key, node.key)) {
                    return (V) node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private Node<K,V>[] resize() {
        if (size >= threshold) {
            Node<K,V>[] oldTable = table;
            Node<K,V> node;
            size = 0;
            int oldCapacity = capacity;
            capacity = capacity * 2;
            table = new Node[capacity];
            for (int i = 0; i < oldCapacity; i++) {
                node = oldTable[i];
                if (node != null) {
                    while (node != null) {
                        put(node.key,node.value);
                        node = node.next;
                    }
                }
            }
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        }
        return table;
    }

    class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
