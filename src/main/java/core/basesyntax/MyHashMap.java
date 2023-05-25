package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int threshold;
    private int size;
    private Node<K, V>[] array;

    public MyHashMap() {
        array = new Node[DEFAULT_CAPACITY];
        threshold = (int) (array.length * DEFAULT_LOAD_FACTOR);
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
        if (size > threshold) {
            final Node<K, V>[] oldArray = array;
            array = new Node[array.length * 2];
            threshold = (int) (array.length * DEFAULT_LOAD_FACTOR);
            size = 0;
            for (Node<K, V> node : oldArray) {
                if (node != null) {
                    Node<K, V> currentNode = node;
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = (key == null) ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
        }
    }
}

