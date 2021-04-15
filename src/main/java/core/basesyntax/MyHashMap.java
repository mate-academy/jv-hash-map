package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int COFFICIENT_GROW = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    private static class Node<K, V> {
        private K key;
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
        Node<K, V> putNode = new Node<>(key, value, null);
        int index = getIndexByKey(key);
        Node<K, V> currentNode = table[index];
        if (table[index] == null) {
            table[index] = putNode;
        } else {
            while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = putNode;
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] tableToResize = table;
        int newCapacity = tableToResize.length * COFFICIENT_GROW;
        threshold *= COFFICIENT_GROW;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        return transfer(tableToResize);
    }

    private Node<K, V>[] transfer(Node<K, V>[] tableToResize) {
        for (Node<K, V> currentNode : tableToResize) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return table;
    }

    private int getIndexByKey(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }
}
