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
        Node<K, V> newNode = nodes[getKeyPosition(key)];
        if (newNode == null) {
            nodes[getKeyPosition(key)] = new Node<>(key, value, null);
            size++;
        }
        while (newNode != null) {
            if (Objects.equals(newNode.key,key)) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                Node<K, V> thisNode = new Node<>(key, value, null);
                newNode.next = thisNode;
                size++;
                return;
            }
            newNode = newNode.next;
        }
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
        Node<K,V>[] newNodes = nodes;
        nodes = new Node[nodes.length * 2];
        for (Node<K, V> newNode : newNodes) {
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private int getKeyPosition(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % nodes.length);
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
