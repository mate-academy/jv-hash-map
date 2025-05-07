package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] nodes;
    private int size = 0;
    private double threshold;

    public MyHashMap() {
        nodes = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    private static class Node<K,V> {
        private V value;
        private Node<K,V> next;
        private K key;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        if (nodes[index] == null) {
            nodes[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = nodes[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = nodes[getIndex(key)];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
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
        Node<K, V>[] mapOld = nodes;
        nodes = (Node<K, V>[]) new Node[(int) (nodes.length * 2)];
        threshold = nodes.length * DEFAULT_LOAD_FACTOR;
        for (Node<K, V> kvNode : mapOld) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % nodes.length);
    }
}
