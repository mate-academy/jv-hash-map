package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] hashtable;
    private int size;

    public MyHashMap() {
        hashtable = new Node[CAPACITY];
        size = 0;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private Node<K, V> getNext() {
            return next;
        }

        private void setValue(V value) {
            this.value = value;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % hashtable.length;
    }

    private void resizeHashMap() {
        int capacity = hashtable.length;
        if (capacity == MAX_CAPACITY) {
            return;
        }
        if (capacity << 1 >= MAX_CAPACITY) {
            capacity = MAX_CAPACITY;
        } else {
            capacity = capacity << 1;
        }
        Node<K, V>[] oldHashTable = hashtable;
        hashtable = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldHashTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= hashtable.length * LOAD_FACTOR) {
            resizeHashMap();
        }
        Node<K, V> newNode = new Node(key, value);
        int index = getIndex(newNode.key);
        if (hashtable[index] == null) {
            hashtable[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = hashtable[index];
        while (node != null) {
            if (Objects.equals(key, node.getKey())) {
                node.setValue(value);
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (index < hashtable.length && hashtable[index] != null) {
            Node<K, V> node = hashtable[index];
            while (node != null) {
                if (Objects.equals(key, node.getKey())) {
                    return node.getValue();
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
