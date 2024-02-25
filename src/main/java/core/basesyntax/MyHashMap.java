package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int COEFFICIENT_OF_EXPANSION = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int PRIME_NUMBER = 31;
    private int size;
    private Node<K, V>[] nodes;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        nodes = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    public void put(K key, V value) {
        int hash = (key == null) ? 0 : hash(key);
        int index = hash % nodes.length;
        Node<K, V> newNode = new Node<>(key, value);
        if (nodes[index] == null) {
            nodes[index] = newNode;
            size++;
        } else {
            Node<K, V> current = nodes[index];
            while (current != null) {
                if (Objects.equals(key, current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    break;
                }
                current = current.next;
            }
        }
        resizeIfNeeded();
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : hash(key) % nodes.length;
        Node<K, V> current = nodes[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        int sizeForResize = (int) (nodes.length * DEFAULT_LOAD_FACTOR);
        if (size == sizeForResize) {
            int newCapacity = nodes.length * COEFFICIENT_OF_EXPANSION;
            Node<K, V>[] newNodes = new Node[newCapacity];
            for (Node<K, V> node : nodes) {
                while (node != null) {
                    int newIndex = (node.key == null) ? 0 : hash(node.key) % newCapacity;
                    Node<K, V> newNode = new Node<>(node.key, node.value);
                    insertNode(newNodes, newIndex, newNode);
                    node = node.next;
                }
            }
            nodes = newNodes;
        }
    }

    private void insertNode(Node<K, V>[] nodes, int index, Node<K, V> newNode) {
        if (nodes[index] == null) {
            nodes[index] = newNode;
        } else {
            Node<K, V> current = nodes[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    private int hash(K key) {
        return PRIME_NUMBER * PRIME_NUMBER + (key == null ? 0 : Math.abs(key.hashCode()));
    }

    static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}
