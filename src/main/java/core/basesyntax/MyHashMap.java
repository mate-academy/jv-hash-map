package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int TABLE_MAGNIFICATION_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hash = keyHash(key);
        int index = indexByHash(hash);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = keyHash(key);
        int index = indexByHash(hash);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(int hash, K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[bucketIndex] != null) {
            Node<K, V> lastNodeOfBucket = getLastNodeOfChain(table[bucketIndex]);
            lastNodeOfBucket.next = newNode;
        } else {
            table[bucketIndex] = newNode;
        }
        increment();
    }

    private Node<K, V> getLastNodeOfChain(Node<K,V> head) {
        if (head.next == null) {
            return head;
        }
        return getLastNodeOfChain(head.next);
    }

    private void increment() {
        size++;
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length * TABLE_MAGNIFICATION_FACTOR;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            for (; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }

    private int keyHash(K key) {
        return key == null ? 0 : key.hashCode() < 0 ? key.hashCode() * -1 : key.hashCode();
    }

    private int indexByHash(int hash) {
        return hash % table.length;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
