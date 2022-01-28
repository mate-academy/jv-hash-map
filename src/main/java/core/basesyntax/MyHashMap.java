package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_CAPACITY = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_CAPACITY);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(int hash) {
        return hash % table.length;
    }

    private void putFirstValue(int index, K key, V value) {
        table[index] = new Node<>(hash(key), key, value, null);
        size++;
    }

    private void putValue(int index, K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(hash(key), key, value, null);
            size++;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = new Node<>(hash(key), key, value, null);
                    break;
                }
                node = node.next;
            }
            size++;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        capacity *= 2;
        threshold *= 2;
        table = new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(hash(key));
        if (size == 0) {
            putFirstValue(index, key, value);
        } else {
            putValue(index, key, value);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(hash(key))];
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
}
