package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_CAPACITY = 0.75f;
    private static final int DEFAULT_SIZE = 16;
    private float threshold;
    private Node<K, V>[] hashTable;
    private int size;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_SIZE];
        threshold = DEFAULT_SIZE * DEFAULT_CAPACITY;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private MyHashMap.Node<K, V> next;

        public Node(K key, V value, MyHashMap.Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = hashTable[hash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            if (node.next.key == key) {
                node.next.value = value;
                return;
            }
            node = node.next;

        }
        hashTable[hash(key)] = new Node<>(key, value, null);

        size++;
    }

    @Override
    public V getValue(K key) {
        if (hashTable[hash(key)] == null) {
            return null;
        }
        return hash(key) >= hashTable.length ? null : checkValue(key).value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % hashTable.length;
    }

    private void resize() {
        int capacity = hashTable.length * 2;
        Node<K, V>[] oldTable = hashTable;
        threshold = capacity * DEFAULT_CAPACITY;
        hashTable = new Node[capacity];
        for (Node<K, V> oldestTo : oldTable) {
            while (oldestTo != null) {
                put(oldestTo.key, oldestTo.value);
                oldestTo = oldestTo.next;
                size--;
            }
        }
    }

    private Node<K, V> checkValue(K key) {
        Node<K, V> equalsKey = hashTable[hash(key)];
        while (!Objects.equals(key, equalsKey.key)) {
            equalsKey = equalsKey.next;
        }
        return equalsKey;
    }
}
