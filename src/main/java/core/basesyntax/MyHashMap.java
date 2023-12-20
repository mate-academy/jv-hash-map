package core.basesyntax;

import static java.util.Objects.hash;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getBucketIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
        }
        if (table[index] != null && !Objects.equals(table[index].key, key)) {
            Node<K, V> next = table[index].next;
            if (next != null) {
                while (next.next != null) {
                    next = next.next;
                }
                next.next = new Node<>(key, value);
            } else {
                table[index].next = new Node<>(key, value);
            }
            size++;
        }
        if (table[index] != null && Objects.equals(table[index].key, key)) {
            table[index].value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(Object key) {
        int index = getBucketIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        table = new Node[oldTab.length << 1];
        size = 0;
        for (Node<K, V> current : oldTab) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getBucketIndex(Object key) {
        int hash = (key == null) ? 0 : hash(key);
        return (table.length - 1) & hash;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
