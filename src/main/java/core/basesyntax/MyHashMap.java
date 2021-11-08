package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int loadFactor;
    private int capacity;
    private int size;

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0 || size + 1 > loadFactor) {
            resize();
        }
        int indexArrayNode = getHash(key);
        Node<K, V> newNode = table[indexArrayNode];
        if (newNode == null) {
            table[indexArrayNode] = new Node<>(key, value, null);
        } else {
            while (true) {
                if (Objects.equals(newNode.key, key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    newNode.next = new Node<>(key, value, null);
                    break;
                }
                newNode = newNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int indexArrayNode = getHash(key);
        Node<K, V> searchNode = table[indexArrayNode];
        while (searchNode != null) {
            if (Objects.equals(searchNode.key, key)) {
                return searchNode.value;
            }
            searchNode = searchNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (table == null || table.length == 0) {
            loadFactor = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
            capacity = DEFAULT_INITIAL_CAPACITY;
            table = new Node[capacity];
        } else {
            capacity = capacity * 2;
            loadFactor = (int) (capacity * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            size = 0;
            for (int i = 0; i < oldTable.length; i++) {
                Node<K, V> newNode = oldTable[i];
                while (newNode != null) {
                    put(newNode.key, newNode.value);
                    newNode = newNode.next;
                }
            }
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }
}
