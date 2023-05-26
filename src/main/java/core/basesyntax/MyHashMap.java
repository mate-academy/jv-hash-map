package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_ARRAY = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] array;

    public MyHashMap() {
        array = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (array[index] == null) {
            array[index] = newNode;
            size++;
            return;
        }
        Node<K, V> present = getPresentNode(key);
        if (present != null) {
            present.value = value;
        } else {
            getLastNode(key).next = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> result = getPresentNode(key);
        if (result != null) {
            return result.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % array.length;
    }

    private Node<K, V> getLastNode(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = array[index];
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private Node<K, V> getPresentNode(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = array[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void resize() {
        if (size > array.length * DEFAULT_LOAD_FACTOR) {
            final Node<K, V>[] oldArray = array;
            array = new Node[array.length * RESIZE_ARRAY];
            size = 0;
            for (Node<K, V> node : oldArray) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

