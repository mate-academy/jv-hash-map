package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

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
        Node<K, V> current = table[getBucketIndex(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final Node<K, V>[] oldTab = table;
        int newCap = table.length << 1;
        threshold = (int) (newCap * LOAD_FACTOR);
        table = new Node[newCap];
        size = 0;
        transfer(oldTab);
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
        int position = getBucketIndex(key);
        Node<K, V> newNode = new Node<>(position, key, value, null);
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

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
