package core.basesyntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size = 0;
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int threshold = (int) (size * DEFAULT_LOAD_FACTOR);

    class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
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
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            growAndTransfer();
        }
        int keyHash = (key == null) ? 0 : key.hashCode();
        int index = Math.abs(keyHash) % table.length;
        Node<K, V> newNode = new Node<>(hashCode(), key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            table[index].next = null;
        } else {
            Node<K, V> bufferNode = table[index];
            if (Objects.equals(bufferNode.getKey(), newNode.getKey())) {
                bufferNode.setValue(newNode.getValue());
                return;
            }
            while (bufferNode.next != null) {
                if (Objects.equals(bufferNode.getKey(), newNode.getKey())) {
                    bufferNode.setValue(newNode.getValue());
                    return;
                }
                bufferNode = bufferNode.next;
            }
            bufferNode.next = newNode;
            newNode.next = null;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int keyHash = (key == null) ? 0 : key.hashCode();
        int index = Math.abs(keyHash) % table.length;
        Node<K, V> bufferNode = table[index];
        if (bufferNode == null) return null;
        while (!(Objects.equals(bufferNode.getKey(), key))) {
            if (bufferNode.next == null) break;
            bufferNode = bufferNode.next;
        }
        return bufferNode.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }


    private void growAndTransfer() {
        int arraySize = table.length * 2;
        Node<K, V>[] buffer = table;
        Node<K, V>[] table = new Node[arraySize];
        for (Node<K, V> node : buffer) {
            if (node == null) continue;
            int nodeKeyHash = (node.getKey() == null) ? 0 : node.getKey().hashCode();
            int index = Math.abs(nodeKeyHash) % table.length;
            if (table[index] == null) {
                table[index] = node;
                table[index].next = null;
            } else {
                Node<K, V> bufferNode = table[index];
                while (bufferNode.next != null) {
                    if (Objects.equals(bufferNode.getKey(), node.getKey())) {
                        bufferNode.setValue(node.getValue());
                        return;
                    }
                    bufferNode = bufferNode.next;
                }
                bufferNode.next = node;
                node.next = null;
            }
        }
    }

}

