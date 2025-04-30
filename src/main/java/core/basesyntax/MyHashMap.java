package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = new Node[capacity];
        size = 0;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >> 16);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int keyIndex = hash % capacity;

        Node<K, V> current = table[keyIndex];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newTable = new Node<>(hash, key, value, table[keyIndex]);
        table[keyIndex] = newTable;
        size++;

        if (size > capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int getHash = hash(key);
        int indexHash = getHash % capacity;

        Node<K, V> getCurrent = table[indexHash];
        while (getCurrent != null) {
            if (Objects.equals(getCurrent.key, key)) {
                return getCurrent.value;
            }
            getCurrent = getCurrent.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = new Node[capacity * 2];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> headNode = table[i];
            while (headNode != null) {
                Node<K, V> next = headNode.next;
                int newIndex = headNode.hash % newCapacity;

                headNode.next = newTable[newIndex];
                newTable[newIndex] = headNode;

                headNode = next;
            }
            table[i] = null;
        }
        capacity = capacity * 2;
        table = newTable;
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
}
