package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int CAPACITY_AND_THRESHOLD_MULTIPLIER = 2;
    private final int capacity;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        table = (Node<K, V>[]) new Node[capacity];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int indexForValue = indexInTable(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[indexForValue] == null) {
            table[indexForValue] = newNode;
            size++;
            return;
        }
        isUniqueKey(newNode);
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int indexWantedValue = indexInTable(key);
        Node<K, V> wantedNode = table[indexWantedValue];
        while (wantedNode != null) {
            if (isEquals(wantedNode.key, key)) {
                return wantedNode.value;
            }
            wantedNode = wantedNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newThreshold = threshold * CAPACITY_AND_THRESHOLD_MULTIPLIER;
        int newCapacity = capacity * CAPACITY_AND_THRESHOLD_MULTIPLIER;
        threshold = newThreshold;
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void isUniqueKey(Node<K, V> current) {
        int indexForValue = indexInTable(current.key);
        Node<K, V> checkedNode = table[indexForValue];
        while (checkedNode.next != null) {
            if (isEquals(current.key, checkedNode.key)) {
                checkedNode.value = current.value;
                return;
            }
            checkedNode = checkedNode.next;
        }
        if (isEquals(current.key, checkedNode.key)) {
            checkedNode.value = current.value;
            return;
        }
        checkedNode.next = current;
        size++;
    }

    private boolean isEquals(K key, K secondKey) {
        return Objects.equals(key, secondKey);
    }

    private int hash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    private int indexInTable(K key) {
        return Math.abs(key == null ? 0 : key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
