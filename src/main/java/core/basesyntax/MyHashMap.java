package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int indexBucket = getIndex(hash(key));
        Node<K, V> current = table[indexBucket];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
        table[indexBucket] = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexBucket = getIndex(hash(key));
        Node<K, V> temp = table[indexBucket];
        while (temp != null) {
            if (Objects.equals(key, temp.key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @SuppressWarnings({"unchecked"})
    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> tempNode : oldTable) {
            while (tempNode != null) {
                Node<K, V> tempNext = tempNode.next;
                put(tempNode.key, tempNode.value);
                tempNode = tempNext;
            }
        }
    }

    private static int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndex(int hash) {
        return Math.abs(hash % table.length);
    }
}
