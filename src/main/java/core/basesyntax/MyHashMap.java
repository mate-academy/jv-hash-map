package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int LENGTH_MULTIPLIER = 2;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] currentHashMap;

    public MyHashMap() {
        currentHashMap = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        increaseLength();
        int bucket = hashCode(key);
        if (currentHashMap[bucket] == null) {
            currentHashMap[bucket] = new Node<>(key, value, null);
            size++;
        }
        Node<K, V> newNode = currentHashMap[bucket];
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            newNode = newNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = currentHashMap[hashCode(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    private void increaseLength() {
        if (size >= currentHashMap.length * LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldTable = currentHashMap;
            currentHashMap = (Node<K, V>[]) new Node[currentHashMap.length * LENGTH_MULTIPLIER];
            for (Node<K, V> check : oldTable) {
                while (check != null) {
                    put(check.key, check.value);
                    check = check.next;
                }
            }
        }
    }

    private int hashCode(K key) {
        return (key == null) ? 0 : Math.abs(hashCode() % currentHashMap.length);
    }

    private class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
