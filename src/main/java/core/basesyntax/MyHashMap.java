package core.basesyntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MULTIPLICATION_INDEX = 2;
    private int size;
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);

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
        Node<K, V> endNode = table[index];
        if (endNode == null) {
            endNode = new Node<>(key, value, null);
            size++;
            return;
        }
        while (endNode.next != null) {
            if (Objects.equals(endNode.getKey(), key)) {
                endNode.setValue(value);
                return;
            }
            endNode = endNode.next;
        }
        endNode.next = new Node<>(key, value, null);
    }

    @Override
    public V getValue(K key) {
        int keyHash = (key == null) ? 0 : key.hashCode();
        int index = Math.abs(keyHash) % table.length;
        Node<K, V> helpNode = table[index];
        while (helpNode != null) {
            if (Objects.equals(key, helpNode.getKey())) {
                return helpNode.getValue();
            }
            helpNode = helpNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }


    private void growAndTransfer() {
        size = 0;
        Node<K, V>[] bufferArray = table;
        table = new Node[bufferArray.length * MULTIPLICATION_INDEX];
        for (Node<K, V> node : bufferArray) {
            while (node != null) {
                put(node.getKey(), node.getValue());
            }
        }
    }
}

