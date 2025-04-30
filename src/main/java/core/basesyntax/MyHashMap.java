package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static class Node<K, V> implements Map.Entry<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    @Override
    public void put(K key, V value) {

        if (size >= threshold) {
            resize();
        }

        int hash = hash(key);
        int index = Math.abs(hash) % table.length;

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && (Objects.equals(current.key, key)
                    || (key != null && key.equals(current.key)))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = Math.abs(hash) % table.length;

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && (Objects.equals(key, current.key))) {
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

        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = Math.abs(node.hash) % newCapacity;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }
}

