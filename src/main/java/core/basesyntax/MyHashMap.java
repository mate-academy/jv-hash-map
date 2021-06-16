package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final static int DEFAULT_CAPACITY = 16;
    private final static float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private static int threshold;
    private static int size;
    private Node<K, V>[] bucketsArray;

    public MyHashMap() {
        bucketsArray = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int position = countIndex(key);
        Node<K, V> currentNode = bucketsArray[position];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        bucketsArray[position] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = countIndex(key);
        Node<K, V> node = bucketsArray[position];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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
        size = 0;
        Node<K, V>[] oldArray = bucketsArray;
        int capacity = bucketsArray.length * RESIZE_COEFFICIENT;
        threshold = threshold * RESIZE_COEFFICIENT;
        bucketsArray = new Node[capacity];
        for (int i = 0; i < oldArray.length; i++) {
            Node<K, V> currentNode = oldArray[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int countIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % bucketsArray.length);
    }
}
