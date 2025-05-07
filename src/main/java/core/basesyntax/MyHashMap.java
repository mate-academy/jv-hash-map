package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final int SIZE_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = defineCell(getHash(key));
        if (table[index] == null) {
            table[index] = new Node<>(null, getHash(key), key, value);
            size++;
        } else {
            Node<K, V> currNode = table[index];
            while (currNode != null) {
                if (Objects.equals(key, currNode.key)) {
                    currNode.value = value;
                    return;
                } else if (currNode.next == null) {
                    currNode.next = new Node<>(null, getHash(key), key, value);
                    break;
                }
                currNode = currNode.next;
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[defineCell(getHash(key))];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int defineCell(int hash) {
        return hash % table.length;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        if (size == (int) (DEFAULT_LOAD_FACTOR * table.length)) {
            transfer(table.length * SIZE_MULTIPLIER);
        }
    }

    private void transfer(int newCapacity) {
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> bucket : oldTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        private final int hash;

        Node(Node<K, V> next, int hash, K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
