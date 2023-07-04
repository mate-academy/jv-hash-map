package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
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
        int hash = hash(key);
        int bucketIndex = hash % factCapacity;

        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> node = table[bucketIndex];

            if (node.hash == hash && Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }

            while (node.next != null) {
                if (node.hash == hash && Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }

            if (node.hash == hash && Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }

            node.next = new Node<>(hash, key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int bucketIndex = hash % factCapacity;
        Node<K, V> tmpNode = table[bucketIndex];

        if (tmpNode == null) {
            return null;
        } else {
            while (tmpNode.next != null) {
                if (Objects.equals(tmpNode.key, key)) {
                    return tmpNode.value;
                }
                tmpNode = tmpNode.next;
            }
        }
        return tmpNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int hash = 0;
        if (key == null) {
            return hash;
        }
        hash = key.hashCode();
        if (hash < 0) {
            hash *= -1;
        }
        return hash;
    }

    private void checkSize() {
        if (size >= factCapacity * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = factCapacity * 2;
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
