package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int i = Math.abs(hash(key) % table.length);
        Node<K, V> current = table[i];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(hash(key), key, value, null);
                size++;
                return;
            }
            current = current.next;
        }
        table[i] = new Node<>(hash(key), key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int i = Math.abs(hash(key) % table.length);
        Node<K, V> temp = table[i];
        while (temp != null) {
            if (Objects.equals(key, temp.key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        int newCap;
        int newThreshold;
        if (oldTab.length >= DEFAULT_INITIAL_CAPACITY) {
            newCap = oldTab.length * 2;
            newThreshold = (int) (newCap * DEFAULT_LOAD_FACTOR);
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        threshold = newThreshold;
        @SuppressWarnings({"unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        for (int j = 0; j < oldTab.length; ++j) {
            Node<K, V> temp = oldTab[j];
            while (temp != null) {
                oldTab[j] = null;
                Node<K, V> tempNext = temp.next;
                int i = Math.abs(temp.hash % newCap);
                temp.next = newTab[i];
                newTab[i] = temp;
                temp = tempNext;
            }
        }
    }

    private static int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }
}
