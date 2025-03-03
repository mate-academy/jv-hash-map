package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_CAPACITY_INCREASE = 1;
    private static final int CONSTANT_FOR_HASH = 0x7FFFFFFF;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> putNode = new Node<>(key, value);
        int indexTable = indexByKey(key);
        if (table[indexTable] != null) {
            Node<K, V> currentNode = table[indexTable];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, putNode.key)) {
                    currentNode.value = putNode.value;
                    return;
                }
                currentNode.next = currentNode.next == null ? putNode : currentNode.next;
                currentNode = currentNode.next == putNode ? null : currentNode.next;
            }
            size++;
            return;
        }
        table[indexTable] = putNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexTable = indexByKey(key);
        if (table[indexTable] != null) {
            Node<K, V> currentNode = table[indexTable];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = table.length << DEFAULT_CAPACITY_INCREASE;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] copyMap = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : copyMap) {
            while (node != null) {
                this.put(node.key, node.value);
                Node<K, V> nextNode = node.next;
                node.next = null;
                node = nextNode;
            }
        }
    }

    private int indexByKey(K key) {
        return key == null ? 0 : key.hashCode() & CONSTANT_FOR_HASH % capacity;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
