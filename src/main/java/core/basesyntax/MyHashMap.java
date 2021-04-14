package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int keyHash = hash(key);
        Node<K, V> node = new Node<>(key, value, null);
        Node<K, V> existingNode = table[keyHash];
        while (existingNode != null) {
            if (Objects.equals(existingNode.key, key)) {
                existingNode.value = value;
                return;
            }
            if (existingNode.next == null) {
                existingNode.next = node;
                size++;
                return;
            }
            existingNode = existingNode.next;
        }
        table[keyHash] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> firstFoundNode = table[hash(key)];
        while (firstFoundNode != null) {
            if (Objects.equals(firstFoundNode.key, key)) {
                return firstFoundNode.value;
            }
            firstFoundNode = firstFoundNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold *= 2;
        size = 0;
        Node<K, V>[] newTable = new Node[newCapacity];
        Node<K, V>[] oldTable = table;
        table = newTable;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
