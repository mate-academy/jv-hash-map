package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    private static class Node<K,V> {
        private final int hash; // = key == null ? 0 : key.hashCode()
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity");
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal load factor");
        }
        this.loadFactor = loadFactor;
        this.table = (Node<K,V>[]) new Node[initialCapacity];
        this.threshold = (int)(initialCapacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int hash = rawHash(key);
        int idx = indexFor(hash, table.length);

        for (Node<K,V> n = table[idx]; n != null; n = n.next) {
            if (n.hash == hash && Objects.equals(n.key, key)) {
                n.value = value;
                return;
            }
        }
        table[idx] = new Node<>(hash, key, value, table[idx]);
        size++;
        if (size >= threshold) {
            resize();
        }

    }

    @Override
    public V getValue(K key) {
        int hash = rawHash(key);
        int idx = indexFor(hash, table.length);
        for (Node<K,V> n = table[idx]; n != null; n = n.next) {
            if (n.hash == hash && Objects.equals(n.key, key)) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static int rawHash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static int indexFor(int hash, int length) {
        int idx = hash % length;
        return (idx < 0) ? idx + length : idx;
    }

    private void resize() {
        int oldCap = table.length;
        int newCap = oldCap << 1;
        Node<K,V>[] oldTab = table;
        Node<K,V>[] newTab = (Node<K,V>[]) new Node[newCap];

        for (int i = 0; i < oldCap; i++) {
            Node<K,V> e = oldTab[i];
            while (e != null) {
                Node<K,V> next = e.next;
                int newIdx = indexFor(e.hash, newCap);
                e.next = newTab[newIdx];
                newTab[newIdx] = e;
                e = next;
            }
        }
        table = newTab;
        threshold = (int)(newCap * loadFactor);
    }
}
