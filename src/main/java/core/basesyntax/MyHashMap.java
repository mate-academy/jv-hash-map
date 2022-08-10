package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float LOAD_FACTOR = 0.75f;
    private transient Node<K, V>[] table;
    private int size;
    private int capacity;
    private double threshhold;

    public MyHashMap() {
        table = new Node[16];
        size = 0;
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshhold = capacity * LOAD_FACTOR;
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshhold) {
            resize();
        }
        int index = hash(key);
        index = Math.abs(index) % capacity;
        if (table[index] == null) {
            table[index] = new Node<>(hash(key), key, value, null);
        } else {
            Node<K, V> node = table[index];
            if (Objects.equals(node.key,key)) {
                node.value = value;
                return;
            }
            while (node.next != null) {
                if (Objects.equals(node.next.key,key)) {
                    node.next.value = value;
                    return;
                }
                node = node.next;
            }
            node.next = new Node<>(hash(key), key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key)) % capacity;
        if (table[index] == null) {
            return null;
        }
        Node<K,V> node = table[index];
        if (Objects.equals(node.key,key)) {
            return node.value;
        }
        while (node.next != null) {
            if (Objects.equals(node.next.key, key)) {
                return node.next.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private final int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[capacity = capacity << 1];
        threshhold = capacity * LOAD_FACTOR;
        int oldSize = size;
        transfer(oldTable);
        size = oldSize;
    }

    private void transfer(Node<K, V>[] nodes) {
        for (Node<K, V> node : nodes) {
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    put(node.next.key, node.next.value);
                    node.next = node.next.next;
                }
            }
        }
    }
}
