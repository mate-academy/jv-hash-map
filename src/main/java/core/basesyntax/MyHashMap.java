package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = getBucketIndex(key);
        Node<K, V> currentNode = table[index];

        if (currentNode == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }

        while (currentNode != null) {
            if (Objects.equals(key, currentNode.getKey())) {
                currentNode.setValue(value);
                return;
            }
            if (currentNode.getNext() == null) {
                currentNode.setNext(new Node<>(key, value, null));
                size++;
                return;
            }
            currentNode = currentNode.getNext();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (Objects.equals(key, currentNode.getKey())) {
                return currentNode.getValue();
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;

        for (Node<K, V> headNode : oldTable) {
            Node<K, V> currentNode = headNode;
            while (currentNode != null) {
                put(currentNode.getKey(), currentNode.getValue());
                currentNode = currentNode.getNext();
            }
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
