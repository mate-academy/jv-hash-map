package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] array;
    private int threshold;
    private int size;

    public MyHashMap() {
        array = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        if (array[getIndexByKey(key)] == null) {
            array[getIndexByKey(key)] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> currentNode = array[getIndexByKey(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode = array[getIndexByKey(key)];
        while (currentNode != null) {
            if (currentNode.key == key || currentNode.key != null && currentNode.key.equals(key)) {
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

    private void resize() {
        size = 0;
        Node<K, V>[] tmpArray = array;
        array = (Node<K,V>[]) new Node[tmpArray.length * RESIZE_FACTOR];
        threshold = (int) (array.length * LOAD_FACTOR);
        for (Node<K, V> tmpNode : tmpArray) {
            while (tmpNode != null) {
                put(tmpNode.key, tmpNode.value);
                tmpNode = tmpNode.next;
            }
        }
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % array.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
