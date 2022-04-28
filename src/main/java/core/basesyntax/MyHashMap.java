package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private Node<K, V>[] nodes;
    private int size;

    public MyHashMap() {
        nodes = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        thresholdCheck();
        Node<K, V> previous = nodes[getIndex(key)];
        if (previous == null) {
            nodes[getIndex(key)] = new Node<K, V>(key, value, null);
            size++;
            return;
        }
        while (previous.next != null || Objects.equals(previous.key,key)) {
            if (Objects.equals(previous.key, key)) {
                previous.value = value;
                return;
            }
            previous = previous.next;
        }
        previous.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return (node == null) ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> node = nodes[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key,key)) {
                break;
            }
            node = node.next;
        }
        return node;
    }

    private void resize() {
        int newSize = nodes.length * 2;
        Node<K, V>[] newNodes = new Node[newSize];
        Node<K, V>[] current = nodes;
        nodes = newNodes;
        size = 0;
        transfer(current);
    }

    private void transfer(Node<K, V>[] current) {
        for (Node<K, V> node : current) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
    
    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % nodes.length);
    }

    private int getThreshold() {
        return (int) (nodes.length * LOAD_FACTOR);
    }

    private void thresholdCheck() {
        if (size > getThreshold()) {
            resize();
        }
    }

    private class Node<K, V> {
        K key;
        V value;
        Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
