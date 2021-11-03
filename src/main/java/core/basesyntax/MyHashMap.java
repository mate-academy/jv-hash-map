package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int nodesSize;
    private Node<K, V>[] nodes;
    private int size;

    {
        nodesSize = DEFAULT_CAPACITY;
        nodes = new Node[nodesSize];
    }

    @Override
    public void put(K key, V value) {
        if (size == nodesSize * LOAD_FACTOR) {
            grow();
        }
        Node<K, V> newNode = nodes[getPosition(key)];
        if (newNode == null) {
            newNode = new Node(key, value, null);
            nodes[getPosition(key)] = newNode;
            size++;
        } else {
            while (newNode != null) {
                if (Objects.equals(newNode.key, key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    Node<K, V> currentNode = new Node<>(key, value, null);
                    newNode.next = currentNode;
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = nodes[getPosition(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    private int getPosition(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % nodesSize);
    }

    private void grow() {
        size = 0;
        Node<K, V>[] copyNodes = nodes;
        nodes = new Node[nodesSize * 2];
        for (Node<K, V> copyNode : copyNodes) {
            while (copyNode != null) {
                put(copyNode.key, copyNode.value);
                copyNode = copyNode.next;
            }
        }
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
