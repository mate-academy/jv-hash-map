package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int NON_NEGATIVE_HASH_MASK = 0x7fffffff;
    private static final int RESIZE_MULTIPLIER = 2;

    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = (key == null) ? 0 : key.hashCode();
        int index = (hash & NON_NEGATIVE_HASH_MASK) % table.length;

        Node<K, V> current = table[index];
        Node<K, V> previous = null;

        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            while (current != null) {
                if (Objects.equals(key, current.key)) {
                    current.value = value;
                    return;
                }
                previous = current;
                current = current.next;
            }
            previous.next = new Node<>(hash, key, value, null);
        }
        size++;
        if (size > threshold) {
            resize();
        }

    }

    @Override
    public V getValue(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        int index = (hash & NON_NEGATIVE_HASH_MASK) % table.length;

        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * RESIZE_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (int bucketIndex = 0; bucketIndex < table.length; bucketIndex++) {
            Node<K, V> current = table[bucketIndex];
            while (current != null) {
                Node<K, V> nextNode = current.next;
                int newIndex = (current.hash & NON_NEGATIVE_HASH_MASK) % newTable.length;
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = nextNode;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }
}
