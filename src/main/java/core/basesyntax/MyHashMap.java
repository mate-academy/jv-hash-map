package core.basesyntax;

import java.util.HashMap;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int capacity;
    Node<K, V>[] table;

    public MyHashMap() {
        size = 0;
        capacity = DEFAULT_CAPACITY;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putWithNullKey(value);
        } else {
            int index = indexFor(getHash(key.hashCode()));
            if (table[index] == null) {
                addEntry(key, value, index);
                size++;
            } else {
                Node<K, V> nodeToCheck = table[index];
                while (nodeToCheck != null) {
                    if (key.equals(nodeToCheck.key)) {
                        nodeToCheck.value = value;
                        return;
                    }
                    nodeToCheck = nodeToCheck.next;
                }
                addEntry(key, value, index);
                size++;
            }
        }
        if (size > capacity * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0] == null ? null : table[0].value;
        }
        int index = indexFor(getHash(key.hashCode()));
        if (index < 0 || index >= table.length) {
            return null;
        }
        Node<K, V> nodeToCheck = table[index];
        while (nodeToCheck != null) {
            if (key.equals(nodeToCheck.key)) {
                return nodeToCheck.value;
            }
            nodeToCheck = nodeToCheck.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                int hash = getHash(node.key.hashCode());
                int index = indexFor(hash);
                table[index] = new Node<K, V>(node.key, node.value, null);
                node = node.next;
            }
        }
    }

    private void putWithNullKey(V value) {
        if (table[0] == null) {
            table[0] = new Node<K, V>(null, value, null);
            size++;
        } else {
            table[0].value = value;
        }
    }

    private void addEntry(K key, V value, int index) {
        Node<K, V> oldEntry = table[index];
        table[index] = new Node<K, V>(key, value, oldEntry);
    }

    private int indexFor(int hash) {
        return hash & (table.length - 1);
    }

    private int getHash(int hashCode) {
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
