package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int currentSize;
    private Node<K, V>[] node;

    public MyHashMap() {
        node = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> currentNode = node[getIndex(key)];
        if (currentNode == null) {
            node[getIndex(key)] = new Node<>(key, value);
            currentSize++;
            return;
        }
        while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(key, value);
        currentSize++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = node[getIndex(key)];
        if (currentNode == null) {
            return null;
        }
        while (!Objects.equals(currentNode.key, key)) {
            currentNode = currentNode.next;
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return currentSize;
    }

    private void resize() {
        if (currentSize > node.length * LOAD_FACTOR) {
            currentSize = node.length * 2;
            Node<K, V>[] oldNode = new Node[currentSize];
            Node<K, V>[] newNode = node;
            node = oldNode;
            currentSize = 0;
            for (Node<K, V> eachNode: newNode) {
                while (eachNode != null) {
                    put(eachNode.key, eachNode.value);
                    eachNode = eachNode.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % node.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
