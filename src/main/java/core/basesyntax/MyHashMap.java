package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DOUBLING_SIZE = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = hash(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (Objects.equals(newNode.key, node.key)) {
                    node.value = newNode.value;
                    break;
                } else if (node.next == null) {
                    node.next = newNode;
                    size++;
                    break;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table.length == 0) {
            return null;
        }
        int index = hash(key);
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

    private int hash(final K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        size = 0;
        int capacity = table.length * DOUBLING_SIZE;
        threshold *= DOUBLING_SIZE;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            for (Node<K, V> node = oldTable[i]; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }
}
