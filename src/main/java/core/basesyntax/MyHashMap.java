package core.basesyntax;

import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int RESIZE_MULTIPLIER = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    public void put(K key, V value) {
        resizeIfExceedCapacity();
        int hash = getHash(key);
        int index = calculateIndex(key, hash);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, hash);
            size++;
            return;
        }
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value, hash);
                break;
            }
            current = current.next;
        }
        size++;
    }

    public V getValue(K key) {
        int hash = getHash(key);
        int index = (key == null) ? 0 : getIndex(hash, table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    private int getIndex(int hash, int tableLength) {
        return (hash & Integer.MAX_VALUE) % tableLength;
    }

    private void resize() {
        int newCapacity = table.length * RESIZE_MULTIPLIER;
        MyHashMap<K, V> newHashMap = new MyHashMap<>();
        newHashMap.table = new Node[newCapacity];
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                newHashMap.put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
        table = newHashMap.table;
    }

    private void resizeIfExceedCapacity() {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int calculateIndex(K key, int hash) {
        return (key == null) ? 0 : getIndex(hash, table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        private Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = null;
        }
    }
}
