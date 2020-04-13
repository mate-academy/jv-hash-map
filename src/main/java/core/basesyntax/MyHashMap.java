package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_RATE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            tableResize();
        }
        int index = indexFor(key, table.length);
        putValue(key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        Node<K, V> node = getNode(key);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private V getForNullKey() {
        for (Node<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }

    private Node<K, V> getNode(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node;
                }
                node = node.next;
            }
        }
        return null;
    }

    private void tableResize() {
        Node<K, V>[] newTable = new Node[table.length * RESIZE_RATE];
        transfer(newTable);
        table = newTable;
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = indexFor(node.key, newTable.length);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
    }

    private int hash(int keyHash) {
        keyHash ^= (keyHash >>> 20) ^ (keyHash >>> 12);
        return keyHash ^ (keyHash >>> 7) ^ (keyHash >>> 4);
    }

    private int indexFor(K key, int tableLength) {
        return key == null ? 0 : hash(key.hashCode()) & (tableLength - 1);
    }

    private void putValue(K key, V value, int index) {
        Node<K, V> found = getNode(key);
        if (found != null) {
            found.value = value;
            return;
        }
        table[index] = new Node<>(key, value, table[index]);
        size++;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
