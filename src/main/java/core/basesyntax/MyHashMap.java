package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
//    private final float loadFactor;

    public MyHashMap() {
//        loadFactor = DEFAULT_LOAD_FACTOR;
    }

    static class Node<K, V> {
        final int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        table = resize();
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        if (table == null || table.length == 0) {
            threshold = (int) (INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            return (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        }
        return null;
    }

    private void transfer() {}
}
