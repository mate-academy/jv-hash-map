package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        this.table = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        putVal(index, key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K,V> result;
        return (result = getNode(key)) == null ? null : result.value;
    }

    @Override
    public int getSize() {
        return size;

    }

    private void putVal(int index, K key, V value) {
        Node<K, V> p = table[index];
        if (p == null) {
            p = new Node<>(key, value, null);
            table[index] = p;
        } else {
            if ((Objects.equals(key, p.key))) {
                p.value = value;
                return;
            }
            while (p.next != null) {
                p = p.next;
                if ((Objects.equals(key, p.key))) {
                    p.value = value;
                    return;
                }
            }
            p.next = new Node<>(key, value, null);
        }
        if (size++ > threshold) {
            resize();
        }
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTab = table;
        int oldCap = oldTab.length;
        capacity = oldCap << 1; //double capacity
        threshold = threshold << 1; // double threshold

        @SuppressWarnings({"unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[capacity];
        table = newTab;
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                while (e != null) {
                    put(e.key,e.value);
                    e = e.next;
                }
            }
        }
    }

    private Node<K,V> getNode(Object key) {
        int index = getIndex(key);
        Node<K,V> result = table[index];
        while (result != null) {
            if (Objects.equals(key, result.key)) {
                return result;
            }
            result = result.next;
        }
        return null;
    }

    private int getIndex(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode())) % capacity;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
