package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        int index = getIndex(key);
        int threshold = (int) (table.length * LOAD_FACTOR);
        if (size > threshold) {
            resizeTable();
        }
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> oldNode = table[index];
            while (oldNode != null) {
                if (keysEquals(key, oldNode.key)) {
                    oldNode.value = value;
                    return;
                }
                if (oldNode.next == null) {
                    oldNode.next = node;
                    break;
                }
                oldNode = oldNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (keysEquals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private boolean keysEquals(K firstKey, K secondKey) {
        return firstKey == null && secondKey == null
                || firstKey != null && firstKey.equals(secondKey);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeTable() {
        size = 0;
        int newCapacity = table.length << 1;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key) % table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
