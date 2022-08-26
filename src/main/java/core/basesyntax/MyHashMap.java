package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_RATE = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        increaseSize();
        int bucketIndex = getBucketIndex(key);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> newNode = new Node<>(key, value, null);
            for (Node<K, V> node = table[bucketIndex]; node != null; node = node.next) {
                if (Objects.equals(node.key, newNode.key)) {
                    node.value = newNode.value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    return;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node = table[getBucketIndex(key)]; node != null; node = node.next) {
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

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void increaseSize() {
        if (size == (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * GROW_RATE];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    size--;
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
