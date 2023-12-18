package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75D;
    private static final int INITIAL_CAPACITY = 16;
    private Node<K,V>[] nodesArray;
    private int size;
    private int treshold;

    public MyHashMap() {
        nodesArray = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> newNode = new Node<>(key, value);
        treshold = (int) (nodesArray.length * LOAD_FACTOR);

        if (size > treshold) {
            resize();
        }

        if (nodesArray[getIndex(key)] == null) {
            nodesArray[getIndex(key)] = newNode;
            size++;
            return;
        }

        for (Node<K,V> node = nodesArray[getIndex(key)]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K,V> node = nodesArray[getIndex(key)]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) object;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(Objects.hash(key)));
    }

    private int getIndex(K key) {
        return hash(key) % nodesArray.length;
    }

    private Node<K,V>[] resize() {
        Node<K,V>[] oldNodesArray = nodesArray;
        nodesArray = new Node[oldNodesArray.length * 2];
        size = 0;
        for (Node<K,V> node : oldNodesArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return nodesArray;
    }
}
