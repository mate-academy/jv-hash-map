package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int bucket = hash(key);
        Node<K, V> newNode = new Node(key, value, null);
        if (table[bucket] == null) {
            table[bucket] = newNode;
        } else {
            for (Node<K, V> node = table[bucket]; node != null; node = node.next) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = newNode;
                    size++;
                    return;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucket = hash(key);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (node.key != null && node.key.equals(key) || node.key == key) {
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

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length << 1];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    this.put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
