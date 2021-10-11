package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    Node<K, V>[] table;
    public int DEFAULT_CAPACITY = 16;
    public int CAPACITY;
    public int size = 0;
    public double LOAD_FACTOR = 0.75;
    public int threshold = 0;

    public void resize() {
        if (table == null || CAPACITY == 0) {
            CAPACITY = DEFAULT_CAPACITY;
            table = new Node[DEFAULT_CAPACITY];
            threshold = (int) (CAPACITY * LOAD_FACTOR);
        } else {
            Node<K, V>[] oldTable = table;
            CAPACITY = CAPACITY * 2;
            table = new Node[CAPACITY];
            threshold = (int) (CAPACITY * LOAD_FACTOR);
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (CAPACITY == 0 || size + 1 > threshold) {
            resize();
        }
        Node<K, V> node = new Node<>();
        node.hash = key == null ? 0 : key.hashCode();
        node.key = key;
        node.value = value;

        Node<K, V> curNode = table[getBucketIndex(node.hash)];
        if (curNode == null) {
            table[Math.abs(node.hash) % CAPACITY] = node;
            size++;
        } else {
            while (curNode.next != null && !keysAreEquals(curNode.key, key)) {
                curNode = curNode.next;
            }
            if (keysAreEquals(curNode.key, key)) {
                curNode.value = value;
            } else {
                curNode.next = node;
                size++;
            }
        }
    }

    private int getBucketIndex(int hash) {
        return Math.abs(hash) % CAPACITY;
    }

    private boolean keysAreEquals(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    @Override
    public V getValue(K key) {
        if (CAPACITY == 0 || size == 0) {
            return null;
        }
        Node<K, V> curNode = table[key == null ? 0 : getBucketIndex(key.hashCode())];
        if (curNode == null) {
            return null;
        }
        while (curNode.next != null && !keysAreEquals(curNode.key, key)) {
            curNode = curNode.next;
        }
        return curNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }
}
