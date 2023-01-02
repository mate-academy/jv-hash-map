package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        Node<K, V> currentNode = table[getBucketNumber(key)];
        if (currentNode == null) {
            table[getBucketNumber(key)] = new Node<>(hash(key), key, value, null);
            size++;
        } else {
            while (currentNode != null) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash(key), key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getBucketNumber(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    public V remove(K key) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && Objects.equals(table[i].key, key) && table[i].next == null) {
                V removedValue = table[i].value;
                table[i] = null;
                size--;
                return removedValue;
            }
            if (table[i] != null && table[i].next != null) {
                while (table[i] != null) {
                    if (Objects.equals(table[i].key, key)) {
                        V removedValue = table[i].value;
                        table[i] = table[i].next;
                        size--;
                        return removedValue;
                    }
                    table[i] = table[i].next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        threshold = threshold << 1;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            if (currentNode != null) {
                if (currentNode.next == null) {
                    put(currentNode.key, currentNode.value);
                } else {
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private int getBucketNumber(K key) {
        return Math.abs(hash(key)) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
