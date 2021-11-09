package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] nodes;
    private int size;
    private int threshold;

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
        threshold = (int) (nodes.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            grow();
        }
        Node<K, V> newNode = nodes[getIndexByKey(key)];
        if (newNode == null) {
            nodes[getIndexByKey(key)] = new Node<>(key, value, null);
        }
        while (newNode != null) {
            if (Objects.equals(newNode.key,key)) {
                newNode.value = value;
                return;
            } else if (newNode.next == null) {
                Node<K, V> thisNode = new Node<>(key, value, null);
                newNode.next = thisNode;
                break;
            }
            newNode = newNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = nodes[getIndexByKey(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private void grow() {
        size = 0;
        Node<K,V>[] oldNode = nodes;
        nodes = new Node[nodes.length * 2];
        for (Node<K, V> newNode : oldNode) {
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % nodes.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
