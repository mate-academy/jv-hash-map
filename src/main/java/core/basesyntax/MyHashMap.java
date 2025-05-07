package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final int RESIZE_CONSTANT = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        resize();
        putElement(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putElement(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);

        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }

        Node<K, V> currentNode = table[index];
        while (currentNode.next != null && !Objects.equals(currentNode.key, key)) {
            currentNode = currentNode.next;
        }
        if (Objects.equals(currentNode.key, key)) {
            currentNode.value = value;
        } else {
            currentNode.next = newNode;
            size++;
        }
    }

    private void resize() {
        int threshold = (int) (table.length * LOAD_FACTOR);
        if (size == threshold) {
            int newCapacity = table.length * RESIZE_CONSTANT;
            Node<K, V>[] tempArrayNode = table;
            table = new Node[newCapacity];
            size = 0;
            for (Node<K, V> currentNode : tempArrayNode) {
                while (currentNode != null) {
                    putElement(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
