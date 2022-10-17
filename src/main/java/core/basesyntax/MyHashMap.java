package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int RESIZE_INDEX = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Node<K,V>[] elements;
    private int size;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold;

    @Override
    public void put(K key, V value) {
        resize();
        int hashValue = hash(key);
        int index = getIndex(key, capacity);
        for (Node<K,V> node = elements[index]; node != null; node = node.next) {
            if (node.hash == hashValue && Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
        }
        putValue(key, value, hashValue,index);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = getIndex(key, capacity);
        for (Node<K, V> node = elements[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key) && node.hash == hash(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(K key, V value, int hashvalue, int index) {
        Node<K,V> nextNode = elements[index];
        elements[index] = new Node<>(key, value, hashvalue, nextNode);
        if (++size >= threshold) {
            resize();
        }
    }

    private int hash(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        return (hashCode < 0) ? -hashCode : hashCode;
    }

    private int getIndex(K key, int capacity) {
        return hash(key) % capacity;
    }

    private void resize() {
        if (elements == null || elements.length == 0) {
            elements = new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        } else if (size >= threshold) {
            Node<K,V>[] newElements = new Node[capacity * RESIZE_INDEX];
            capacity *= RESIZE_INDEX;
            transfer(newElements, elements);
            elements = newElements;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        }
    }

    private void transfer(Node<K,V>[] newElements, Node<K,V>[] elements) {
        for (Node<K, V> element : elements) {
            Node<K, V> node = element;
            if (node != null) {
                do {
                    Node<K, V> nextNoda = node.next;
                    int index = getIndex(node.key, capacity);
                    node.next = newElements[index];
                    newElements[index] = node;
                    node = nextNoda;
                } while (node != null);
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, int hash,Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
