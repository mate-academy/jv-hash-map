package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_VALUE = 2;
    private int size;
    private float loadFactor;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        Node<K, V> node = new Node<>(key, value);
        int indexBucket = getIndex(key);
        if (table[indexBucket] == null) {
            table[indexBucket] = node;
        } else {
            Node<K, V> current = table[indexBucket];
            Node<K, V> prev = null;
            while (current != null) {
                if (Objects.equals(current.key, node.key)) {
                    current.value = node.value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexBucket = getIndex(key);
        if (table[indexBucket] != null) {
            Node<K, V> current = table[indexBucket];

            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private void resizeIfNeeded() {
        loadFactor = (float) size / table.length;
        if (loadFactor > DEFAULT_LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * INCREASE_VALUE];
            size = 0;
            for (Node<K, V> kvNode : oldTable) {
                Node<K, V> current = kvNode;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
