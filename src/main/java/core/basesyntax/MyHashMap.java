package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkThreshold();
        putValue(hash(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> newNode = table[index];
        while (newNode != null) {
            if (Objects.equals(key, newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
            return;
        }
        Node<K, V> node = table[hash];
        while (node.next != null) {
            if (Objects.equals(key, node.key)) {
                break;
            }
            node = node.next;
        }
        if (Objects.equals(key, node.key)) {
            node.value = value;
            size--;
            return;
        }
        node.next = new Node<>(key, value, null);
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> newNode = node;
            while (newNode != null) {
                putValue(hash(newNode.key), newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private void checkThreshold() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

