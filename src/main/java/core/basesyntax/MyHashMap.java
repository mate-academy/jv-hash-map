package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private float loadFactor;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        loadFactor = DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int index = indexFor(hash(key), table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(hash(key), table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private int hash(Object key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> node = table[index];
        table[index] = new Node<>(key, value, node);
        size++;
        if (size > table.length * loadFactor) {
            resize(table.length * 2);
        }
    }

    private void putForNullKey(V value) {
        Node<K, V> node = table[0];
        while (node != null) {
            if (node.key == null) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(null, value, 0);
    }

    private void resize(int newCapacity) {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        transfer(newTable);
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (node.key == null) {
                    Node<K, V> nextNode = newTable[0];
                    newTable[0] = new Node<>(null, node.value, nextNode);
                }

                int newIndex = indexFor(hash(node.key), newTable.length);
                Node<K, V> nextNode = newTable[newIndex];
                newTable[newIndex] = new Node<>(node.key, node.value, nextNode);

                node = node.next;
            }
        }
        table = newTable;
    }

    private class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
