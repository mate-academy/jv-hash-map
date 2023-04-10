package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_COEFFICIENT = 2;
    private Node<K, V>[] elements;
    private int threshold;
    private int size;

    public MyHashMap() {
        elements = new Node[DEFAULT_CAPACITY];
        threshold = (int) (elements.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> existingNode = elements[index];
        Node<K, V> newNode = new Node<>(getHash(key), key, value, null);
        if (Objects.isNull(existingNode)) {
            elements[index] = newNode;
        }
        while (Objects.nonNull(existingNode)) {
            if (Objects.equals(existingNode.key, key)) {
                existingNode.value = value;
                return;
            }
            if (Objects.isNull(existingNode.next)) {
                existingNode.next = newNode;
                break;
            }
            existingNode = existingNode.next;
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> resultNode;
        for (Node<K, V> element : elements) {
            resultNode = element;
            while (Objects.nonNull(resultNode)) {
                if (Objects.equals(resultNode.key, key)) {
                    return resultNode.getValue();
                }
                resultNode = resultNode.next;
            }
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

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        final Node<K, V>[] oldElements = elements;
        elements = new Node[elements.length * GROW_COEFFICIENT];
        threshold = (int) (elements.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (Node<K, V> oldElement : oldElements) {
            while (Objects.nonNull(oldElement)) {
                put(oldElement.getKey(), oldElement.getValue());
                oldElement = oldElement.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }
    }
}
