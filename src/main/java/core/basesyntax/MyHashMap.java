package core.basesyntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    class Node<K, V> {
        final K key;
        V value;
        Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            growAndTransfer();
        }
        int keyHash = (key == null) ? 0 : key.hashCode();
        int index = Math.abs(keyHash) % table.length;
        Node<K, V> newNode = table[index];
        while (newNode != null) {
            if (Objects.equals(key, newNode.getKey())) {
                newNode.setValue(value);
                return;
            } else if (newNode.next == null) {
                newNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            newNode = newNode.next;
        }
        if (newNode == null) {
            table[index] = new Node<>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int keyHash = (key == null) ? 0 : key.hashCode();
        int index = Math.abs(keyHash) % table.length;
        Node<K, V> bufferNode = table[index];
        while (bufferNode != null) {
            if (Objects.equals(key, bufferNode.getKey())) {
                return bufferNode.getValue();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }


    private void growAndTransfer() {
        int arraySize = table.length * 2;
        Node<K, V>[] buffer = table;
        table = new Node[arraySize];
        for (Node<K, V> node: buffer) {
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.next;
            }
        }
    }
}

