package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int TABLE_MAGNIFICATION_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> node = findNodeByKey(key);
        if (node == null) {
            addNode(key, value);
        } else {
            node.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = findNodeByKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNodeByKey(K key) {
        int hash = keyHash(key);
        int index = indexByHash(hash);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }

    private void addNode(K key, V value) {
        int hash = keyHash(key);
        int index = indexByHash(hash);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[index] != null) {
            Node<K, V> lastNodeOfBucket = getLastNodeOfChain(table[index]);
            lastNodeOfBucket.next = newNode;
        } else {
            table[index] = newNode;
        }
        increment();
    }

    private Node<K, V> getLastNodeOfChain(Node<K,V> head) {
        if (head.next == null) {
            return head;
        }
        return getLastNodeOfChain(head.next);
    }

    private void increment() {
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = table.length * TABLE_MAGNIFICATION_FACTOR;
        threshold = (int)(newCapacity * DEFAULT_LOAD_FACTOR);
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            for (; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }

    private int keyHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int indexByHash(int hash) {
        return hash % table.length;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
