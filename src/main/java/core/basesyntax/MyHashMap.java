package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY = 16;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        threshold = (int) (DEFAULT_LOAD_FACTOR * CAPACITY);
        table = new Node[CAPACITY];
    }

    public int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[hash(key)] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
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

    public void resize() {
        size = 0;
        int newCap = table.length << 1;
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCap);
        Node<K,V>[] oldTab = table;
        table = new Node[newCap];
        for (Node<K, V> transfer : oldTab) {
            while (transfer != null) {
                put(transfer.key, transfer.value);
                transfer = transfer.next;
            }
        }
    }
}
