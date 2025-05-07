package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K,V>[] nodes;
    private int size;

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= nodes.length * LOAD_FACTOR) {
            grow();
        }
        int index = getKeyPosition(key);
        Node<K, V> currentNode = nodes[index];
        if (currentNode == null) {
            nodes[index] = new Node<>(key, value, null);
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key,key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                break;
            }
            currentNode = currentNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = nodes[getKeyPosition(key)];
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
        Node<K,V>[] oldNodes = nodes;
        nodes = new Node[nodes.length * 2];
        for (Node<K, V> node : oldNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getKeyPosition(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % nodes.length);
    }

    private static class Node<K, V> {
        private V value;
        private K key;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
