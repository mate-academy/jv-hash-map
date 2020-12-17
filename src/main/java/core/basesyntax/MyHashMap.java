package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = table[indexOfBucket(key)];
        while (newNode != null) {
            if (Objects.equals(key, newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(K key, V value) {
        int currentIndex = indexOfBucket(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[currentIndex] == null) {
            table[currentIndex] = newNode;
        } else {
            Node<K, V> currentNode = table[currentIndex];
            while (currentNode.next != null || Objects.equals(currentNode.key, newNode.key)) {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int indexOfBucket(K key) {
        return hash(key) % table.length;
    }

    private void resize() {
        size = 0;
        threshold = (int) ((table.length * 2) * LOAD_FACTOR);
        Node<K, V>[] newNode = table;
        table = new Node[newNode.length * 2];
        for (int i = 0; i < newNode.length; i++) {
            Node<K, V> tempNode = newNode[i];
            while (tempNode != null) {
                putValue(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
