package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeAndTransfer();
        int index = getIndexFor(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> tempNode = table[index];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                tempNode.value = value;
                return;
            }
            if (tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                return;
            }
            tempNode = tempNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexFor(key);
        Node<K, V> tempNode = table[index];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexFor(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resizeAndTransfer() {
        if (threshold != size) {
            return;
        }
        size = 0;
        int newSize = table.length * RESIZE_FACTOR;
        threshold = (int) (newSize * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[newSize];
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
