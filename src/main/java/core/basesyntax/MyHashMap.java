package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] nodes;
    private int size;

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= nodes.length * LOAD_FACTOR) {
            grow();
        }
        int index = getIndexByKey(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (nodes[index] == null) {
            nodes[index] = newNode;
            size++;
            return;
        }
        Node<K, V> tempNode = nodes[index];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                tempNode.value = value;
                return;
            }
            if (tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                return;
            }
            tempNode = tempNode.next;
        }
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
        for (Node<K, V> node : oldNode) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
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
