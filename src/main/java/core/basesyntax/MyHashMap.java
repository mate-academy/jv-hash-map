package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

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
        int hash = hash(key);
        if (threshold == 0) {
            getDefaultThreshold();
        }
        if (size == threshold) {
            table = resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<K, V> previous = null;
            Node<K, V> current = table[hash];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                previous = current;
                current = current.next;
            }
            previous.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        if (table[hash] == null) {
            return null;
        } else {
            Node<K, V> checkNode = table[hash];
            while (checkNode != null) {
                if (Objects.equals(checkNode.key, key)) {
                    return checkNode.value;
                }
                checkNode = checkNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode()) % table.length);
    }

    private Node<K, V>[] resize() {
        int newLength = table.length * 2;
        Node<K, V>[] newTable = new Node[newLength];
        for (Node<K, V> node : table) {
            while (node != null) {
                int newHash = (node.key == null)
                        ? 0 : Math.abs(node.key.hashCode()) % newLength;
                Node<K, V> next = node.next;
                node.next = newTable[newHash];
                newTable[newHash] = node;
                node = next;
            }
        }
        threshold = (int) (newLength * DEFAULT_LOAD_FACTOR);
        return newTable;
    }

    private void getDefaultThreshold() {
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }
}
