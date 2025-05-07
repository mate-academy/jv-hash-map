package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_ARRAY_SIZE = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] elements;
    private int size;

    public MyHashMap() {
        elements = new Node[DEFAULT_ARRAY_SIZE];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int bucket = getBucket(key, elements);
        Node<K, V> node = checkBucket(elements[bucket], key);
        if (node != null) {
            node.value = value;
            return;
        }
        if (elements == this.elements) {
            size++;
        }
        Node<K, V> currentNode = elements[bucket];
        while (currentNode != null) {
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                return;
            }
            currentNode = currentNode.next;
        }
        elements[bucket] = new Node<>(key, value);
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(key, elements);
        Node<K, V> node = checkBucket(elements[bucket], key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> checkBucket(Node<K, V> node, K key) {
        Node<K, V> currentNode = node;
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int getBucket(K key, Node<K, V>[] list) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % list.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private void resize() {
        if (size > elements.length * LOAD_FACTOR) {
            Node<K, V>[] temp = elements;
            elements = new Node[elements.length * 2];
            size = 0;
            for (Node<K, V> node : temp) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
