package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_COEFFICIENT = 2;
    private Node<K, V>[] elements;
    private int size;

    public MyHashMap() {
        elements = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= elements.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> existingNode = elements[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (existingNode == null) {
            elements[index] = newNode;
            size++;
        }
        while (existingNode != null) {
            if (Objects.equals(existingNode.key, key)) {
                existingNode.value = value;
                return;
            }
            if (existingNode.next == null) {
                existingNode.next = newNode;
                size++;
                break;
            }
            existingNode = existingNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> resultNode = elements[getIndex(key)];
        while (resultNode != null) {
            if (Objects.equals(resultNode.key, key)) {
                return resultNode.value;
            }
            resultNode = resultNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key) % elements.length);
    }

    private void resize() {
        final Node<K, V>[] oldElements = elements;
        elements = new Node[elements.length * GROW_COEFFICIENT];
        size = 0;
        for (Node<K, V> oldElement : oldElements) {
            while (oldElement != null) {
                put(oldElement.key, oldElement.value);
                oldElement = oldElement.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
