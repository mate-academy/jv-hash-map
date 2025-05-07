package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int hash = hash(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node node = table[hash];
            while (node.next != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (Objects.equals(key, node.key)) {
                node.value = value;
            } else {
                node.next = new Node<>(hash, key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node node = table[hash];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void checkSize() {
        if (size >= threshold) {
            Node<K,V>[] oldTable = table;
            size = 0;
            table = new Node[table.length * 2];
            for (Node<K,V> node: oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        }
    }

    private class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
