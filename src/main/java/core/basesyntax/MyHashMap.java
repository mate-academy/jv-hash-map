package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] hashTable;
    private int size;

    public MyHashMap() {
        this.hashTable = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > (int) (LOAD_FACTOR * hashTable.length)) {
            resize();
        }
        int hash = getHash(key);
        int index = getIndex(key);
        Node<K, V> newHashTable = new Node<>(key, value, null);
        Node<K,V> node = hashTable[index];
        if (node == null) {
            hashTable[index] = newHashTable;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newHashTable;
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getIndex(key);
        Node<K,V> node = hashTable[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode() % hashTable.length;
    }

    private int getIndex(K key) {
        return getHash(key) & hashTable.length - 1;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[hashTable.length * 2];
        for (Node<K, V> table : oldHashTable) {
            while (table != null) {
                put(table.key, table.value);
                table = table.next;
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

