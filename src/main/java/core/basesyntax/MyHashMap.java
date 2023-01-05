package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_COEFFICIENT = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizing();
        int bucket = getIndex(key);
        Node<K, V> node = table[bucket];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (node == null) {
            table[bucket] = newNode;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            } else if (node.next == null) {
                node.next = newNode;
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucket = getIndex(key);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizing() {
        if (threshold == size) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * DEFAULT_COEFFICIENT];
            threshold = (int) (table.length * LOAD_FACTOR);
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        int index = getHashCode(key) % table.length;
        if (index < 0) {
            index = -index;
        }
        return index;
    }

    private int getHashCode(K key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode()) ^ (key.hashCode() >>> 16);
    }
}
