package core.basesyntax;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        int index = index(hash(key));
        Node<K, V> node = table[index];
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

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs(h = key.hashCode()) ^ (h >>> 16);
    }

    private int index(int hash) {
        return hash % table.length;
    }

    private void putVal(int hash, K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = index(hash);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
            return;
        }
        while (node != null) {
            if (node.hash == hash
                    && node.key == key
                    || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(hash, key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length * 2;
        threshold = threshold * 2;
        table = (Node<K, V>[]) new Node[newCapacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
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
