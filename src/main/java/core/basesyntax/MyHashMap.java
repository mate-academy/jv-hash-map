package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K,V> {
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

    private void resize() {
        size = 0;
        threshold *= 2;
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[table.length * 2];
        table = newTable;
        for (Node<K, V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hashValue(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(int hashValue) {
        return hashValue % table.length;
    }

    private void putValue(int index, K key, V value) {
        table[index] = new Node<>(hashValue(key), key, value, null);
        size++;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int hash = hashValue(key);
        int index = getIndex(hash);
        Node<K, V> node = table[index];
        if (node == null) {
            putValue(index, key, value);
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = new Node<>(hash, key, value, null);
                    break;
                }
                node = node.next;
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(hashValue(key));
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
}
