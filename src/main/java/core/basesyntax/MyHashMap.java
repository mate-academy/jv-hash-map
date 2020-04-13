package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] nodes;
    private int size;
    private double threshold;

    public MyHashMap() {
        nodes = new Node[INITIAL_CAPACITY];
        size = 0;
        threshold = INITIAL_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getBucketNumber(key);
        Node<K, V> existing = getNode(key, index);
        if (existing == null) {
            Node<K, V> newNode = new Node<>(key, value, nodes[index]);
            nodes[index] = newNode;
            size++;
        } else {
            existing.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key, getBucketNumber(key));
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = nodes.length * 2;
        threshold = newCapacity * LOAD_FACTOR;
        Node<K, V>[] newNodes = new Node[newCapacity];
        Node<K, V>[] oldNodes = nodes;
        nodes = newNodes;
        size = 0;
        for (Node<K, V> node : oldNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K, V> getNode(K key, int index) {
        Node<K, V> node = nodes[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int getBucketNumber(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % nodes.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
