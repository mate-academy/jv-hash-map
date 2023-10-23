package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_SIZE_INCREASE = 2;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        int insertionIndex = getBucket(key);
        Node<K, V> existingNode = table[insertionIndex];
        if (existingNode == null) {
            table[insertionIndex] = node;
            size++;
        } else {
            while (existingNode.next != null) {
                if (Objects.equals(existingNode.key, key)) {
                    existingNode.value = value;
                    return;
                }
                existingNode = existingNode.next;
            }
            if (Objects.equals(existingNode.key, key)) {
                existingNode.value = value;
            } else {
                existingNode.next = node;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int retrievalIndex = getBucket(key);
        Node<K, V> node = table[retrievalIndex];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private void resize() {
        int newCapacity = table.length * DEFAULT_SIZE_INCREASE;
        threshold = threshold * DEFAULT_SIZE_INCREASE;
        Node<K,V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBucket(K key) {
        return getHash(key) % table.length;
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }
}
