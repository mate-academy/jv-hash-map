package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = Math.abs(hash(key));
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] != null) {
            for (Node<K, V> currentNode = table[index]; currentNode != null;
                    currentNode = currentNode.next) {
                if ((currentNode.key == newNode.key)
                        || (currentNode.key != null && currentNode.key.equals(newNode.key))) {

                    currentNode.value = newNode.value;
                    break;
                } else if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                }
            }
        } else {
            table[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key));
        Node<K, V> currentNode = table[index];
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
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() % DEFAULT_INITIAL_CAPACITY;
    }

    private void resize() {
        int newCapacity = table.length * GROW_FACTOR;
        threshold = (int) (LOAD_FACTOR * newCapacity);
        final Node<K, V>[] oldTable = table;
        size = 0;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
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
