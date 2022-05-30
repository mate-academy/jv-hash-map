package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V> [] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V> [])new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int numberOfBucket = hash(key) % table.length;
        if (table[numberOfBucket] != null) {
            Node<K, V> currentElement = table[numberOfBucket];
            if (Objects.equals(currentElement.key, key)) {
                currentElement.value = value;
                return;
            } else {
                while (currentElement.next != null) {
                    if (Objects.equals(currentElement.key, key)) {
                        currentElement.value = value;
                        return;
                    }
                    currentElement = currentElement.next;
                }
                currentElement.next = new Node<>(key, value);
            }
        } else {
            table[numberOfBucket] = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int numberOfBucket = hash(key) % table.length;
        Node<K, V> element = table[numberOfBucket];
        while (element != null) {
            if (Objects.equals(element.key, key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V> [] oldTable = table;
        int oldCapacity = table.length;
        int oldThreshold = threshold;
        int newCapacity = oldCapacity << 1;
        int newThreshold = oldThreshold << 1;
        this.table = (Node<K, V> [])new Node[newCapacity];
        this.threshold = newThreshold;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<K, V> currentElement = oldTable[i].next;
                while (currentElement != null) {
                    put(currentElement.key, currentElement.value);
                    currentElement = currentElement.next;
                }
            }
            oldTable[i] = null;
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()));
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
