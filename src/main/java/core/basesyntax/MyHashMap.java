package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] hashTable;
    private int size;

    public MyHashMap() {
        hashTable = new Node[CAPACITY];
        size = 0;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private Node<K, V> getNext() {
            return next;
        }

        private void setValue(V value) {
            this.value = value;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(hashCode()) % hashTable.length;
    }

    private void resizeHashMap() {
        int capacity = hashTable.length;
        if (capacity == MAX_CAPACITY) {
            return;
        }
        if (capacity << 1 >= MAX_CAPACITY) {
            capacity = MAX_CAPACITY;
        } else {
            capacity = capacity << 1;
        }
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldHashTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= hashTable.length * LOAD_FACTOR) {
            resizeHashMap();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(newNode.key);
        if (hashTable[index] == null) {
            hashTable[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = hashTable[index];
        while (node != null) {
            if (Objects.equals(key, node.getKey())) {
                node.setValue(value);
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (index < hashTable.length && hashTable[index] != null) {
            Node<K, V> node = hashTable[index];
            while (node != null) {
                if (Objects.equals(key, node.getKey())) {
                    return node.getValue();
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
}
