package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
            threshold = (int) (table.length * LOAD_FACTOR);
        }
        putVal(key, value);
    }

    private void putVal(K key, V value) {
        int currentPosition = getIndex(key);
        Node<K, V> tempNode = new Node<>(key, value, null);
        if (table[currentPosition] == null) {
            table[currentPosition] = tempNode;
            size++;
        } else {
            Node<K, V> iterationNode = table[currentPosition];
            while (iterationNode.next != null || Objects.equals(iterationNode.key, tempNode.key)) {
                if (Objects.equals(iterationNode.key, tempNode.key)) {
                    iterationNode.value = tempNode.value;
                    return;
                }
                iterationNode = iterationNode.next;
            }
            iterationNode.next = tempNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            if (currentNode != null) {
                while (currentNode != null) {
                    putVal(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        int hash = hash(key);
        return hash % (table.length - 1);
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
    }
}
