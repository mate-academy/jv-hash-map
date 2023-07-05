package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K,V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int hash = toHash(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K,V> node = table[hash];
        if (Objects.equals(key, node.key)) {
            node.value = value;
            return;
        }
        while (node.next != null) {
            node = node.next;
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
        }
        node.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = getNodeByKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int toHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        int newCapacity = table.length << 1;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K,V> oldNode : oldTable) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private Node<K,V> getNodeByKey(K key) {
        int hash = toHash(key);
        Node<K,V> node = table[hash];
        if (node == null) {
            return null;
        }
        do {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        } while (node != null);
        return null;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
