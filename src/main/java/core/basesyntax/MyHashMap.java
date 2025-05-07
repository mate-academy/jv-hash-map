package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int VALUE_FOR_INCREASE = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEAULT_CAPACITY = 16;
    private Node<K, V>[] entryTable;
    private int size;
    private int threshold;

    public MyHashMap() {
        entryTable = new Node[DEAULT_CAPACITY];
        threshold = (int) (entryTable.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int numberOfBucket = getBucket(key);
        if (size >= threshold) {
            changeSize();
        }
        for (Node<K, V> element = entryTable[numberOfBucket];
                element != null; element = element.next) {
            if (Objects.equals(key, element.key)) {
                element.value = value;
                return;
            }
            if (element.next == null) {
                element.next = new Node(key, value, null);
                size++;
                return;
            }
        }

        entryTable[numberOfBucket] = new Node(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int numberOfBucket = getBucket(key);
        Node<K, V> element = entryTable[numberOfBucket];
        while (element != null) {
            if (Objects.equals(key, element.key)) {
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

    private void changeSize() {
        threshold = (int) (entryTable.length * VALUE_FOR_INCREASE * DEFAULT_LOAD_FACTOR);
        makeTransfer();
    }

    private void makeTransfer() {
        Node<K, V>[] oldTable = entryTable;
        entryTable = new Node[entryTable.length * VALUE_FOR_INCREASE];
        size = 0;
        for (Node<K, V> element : oldTable) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private int getBucket(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % entryTable.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
