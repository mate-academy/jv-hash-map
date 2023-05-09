package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = hash(key);
        Node<K,V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[hash(key)];
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
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        int newLength = oldTable.length * 2;
        table = new Node[newLength];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
