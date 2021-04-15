package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_FACTOR = 0.75f;
    private static final int MULTI = 2;
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
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_FACTOR);
    }

    @Override
    public void put(K key, V value) {

        if (size >= threshold) {
            resize();
        }
        int keyHash = hash(key);
        Node<K, V> node = new Node<>(key, value, null);
        Node<K, V> existNode = table[keyHash];
        while (existNode != null) {
            if (Objects.equals(existNode.key, key)) {
                existNode.value = value;
                return;
            }
            if (existNode.next == null) {
                existNode.next = node;
                size++;
                return;
            }
            existNode = existNode.next;
        }
        table[keyHash] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> foundNode = table[hash(key)];
        while (foundNode != null) {
            if (Objects.equals(foundNode.key, key)) {
                return foundNode.value;
            }
            foundNode = foundNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * MULTI;
        threshold *= MULTI;
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
