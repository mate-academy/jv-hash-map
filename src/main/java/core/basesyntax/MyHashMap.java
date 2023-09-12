package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_RATE = 2;
    private int factCapacity;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        factCapacity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int hash = getHash(key);
        int bucketIndex = hash % factCapacity;

        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> node = table[bucketIndex];

            while (node.next != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }

                node = node.next;
            }

            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }

            node.next = new Node<>(hash, key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int bucketIndex = hash % factCapacity;
        Node<K, V> tmpNode = table[bucketIndex];

        while (tmpNode != null) {
            if (Objects.equals(tmpNode.key, key)) {
                return tmpNode.value;
            }
            tmpNode = tmpNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        int hash;
        if (key == null) {
            return 0;
        }
        hash = key.hashCode();
        return Math.abs(hash);
    }

    private void checkSize() {
        if (size >= factCapacity * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = factCapacity * GROWTH_RATE;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newBucketIndex = node.hash % newCapacity;
                Node<K, V> nextNode = node.next;
                node.next = newTable[newBucketIndex];
                newTable[newBucketIndex] = node;
                node = nextNode;
            }
        }

        table = newTable;
        factCapacity = newCapacity;
    }

    private static class Node<K, V> {
        private final K key;
        private int hash;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }
}
