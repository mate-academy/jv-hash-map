package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[calculatePosition(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = table.length << 1;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        table = new Node[newCapacity];
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            if (node == null) {
                continue;
            }
            Node<K, V> current = node;
            while (current != null) {
                putValue(current.key, current.value);
                current = current.next;
            }
        }
    }

    private void putValue(K key, V value) {
        int position = calculatePosition(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[position] == null) {
            table[position] = newNode;
        } else {
            Node<K, V> current = table[position];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    size--;
                    break;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    private int calculatePosition(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
