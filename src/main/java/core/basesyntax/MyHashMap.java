package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private Node<K,V>[] nodeArray;
    private int size;
    private int threshold;

    public MyHashMap() {
        nodeArray = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> newNode = new Node<>(key, value);
        threshold = (int) (nodeArray.length * LOAD_FACTOR);

        if (size > threshold) {
            resize();
        }

        if (nodeArray[getIndex(key)] == null) {
            nodeArray[getIndex(key)] = newNode;
            size++;
            return;
        }

        for (Node<K,V> node = nodeArray[getIndex(key)]; node != null; node = node.next) {
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
        for (Node<K,V> node = nodeArray[getIndex(key)]; node != null; node = node.next) {
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

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(Objects.hashCode(key)));
    }

    private Node<K, V>[] resize() {
        Node<K,V>[] oldNodeArray = nodeArray;
        nodeArray = new Node[oldNodeArray.length * 2];
        size = 0;
        for (Node<K,V> node : oldNodeArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return nodeArray;
    }

    private int getIndex(K key) {
        return hash(key) % nodeArray.length;
    }
}
