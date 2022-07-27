package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private int threshold;
    private int size;
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
        int position = countIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (bucketsArray[position] == null) {
            bucketsArray[position] = newNode;
        } else {
            Node<K, V> currentNode = bucketsArray[position];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
        if (size >= threshold) {
            resize();
        }
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
        threshold *= RESIZE_COEFFICIENT;
        int capacity = bucketsArray.length * RESIZE_COEFFICIENT;
        Node<K, V>[] oldArray = bucketsArray;
        bucketsArray = new Node[capacity];
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int countIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % bucketsArray.length);
    }
}
