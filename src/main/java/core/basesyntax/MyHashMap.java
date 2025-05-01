package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double loadFactor = 0.75;
    private int size = 0;
    private int currentCapacity = INITIAL_CAPACITY;
    private double maxThreshold = currentCapacity * loadFactor;
    private Node<K, V>[] table = new Node[currentCapacity];

    @Override
    public void put(K key, V value) {
        sizeChecker();
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);

        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (table[index] == null) {
            return null;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        final int oldCapacity = currentCapacity;
        currentCapacity = currentCapacity * 2;
        maxThreshold = currentCapacity * loadFactor;
        Node<K, V>[] oldTable = table;
        table = new Node[currentCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                int index = getIndex(node.key);
                Node<K, V> next = node.next;
                node.next = table[index];
                table[index] = node;
                node = next;
            }
        }
    }

    public int hashCode(K key) {
        if (key == null) {
            return 0;
        }
        return 31 * key.hashCode();
    }

    public int getIndex(K key) {
        return Math.abs(hashCode(key)) % currentCapacity;
    }

    public void sizeChecker() {
        if (size == (int) maxThreshold) {
            resize();
        }
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
