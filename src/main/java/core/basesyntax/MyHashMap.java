package core.basesyntax;

import java.util.Objects;
/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] nodes;
    private int threshold;
    private int size;

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = indexFor(key);
        Node<K, V> node = new Node<>(key, value);
        if (nodes[index] == null) {
            nodes[index] = node;
            size++;
            return;
        }
        Node<K, V> currentNode = nodes[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, node.key)) {
                currentNode.value = node.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
        Node<K, V> currentNode = nodes[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    public int indexFor(K key) {
        return key == null ? 0 : (key.hashCode()) & (nodes.length - 1);
    }

    private void resize() {
        size = 0;
        Node<K, V>[]tempBucket = nodes;
        nodes = (Node<K, V>[]) new Node[nodes.length * 2];
        threshold = (int) (nodes.length * LOAD_FACTOR);
        for (Node<K, V> node : tempBucket) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
