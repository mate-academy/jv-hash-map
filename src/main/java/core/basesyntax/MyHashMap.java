package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfRequired();
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        for (Node<K, V> node = table[index]; node != null;
                node = node.next) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node(key, value);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> currentNode = table[index]; currentNode != null;
                currentNode = currentNode.next) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null ? 0 : ((int)(Math.pow(key.hashCode(), 2)))) % table.length;
    }

    private void resizeIfRequired() {
        if (size >= (table.length * DEFAULT_LOAD_FACTOR)) {
            int capacity = table.length << 1;
            size = 0;
            Node<K, V>[] oldTab = table;
            table = new Node[capacity];
            for (Node<K, V> bucket : oldTab) {
                while (bucket != null) {
                    put(bucket.key, bucket.value);
                    bucket = bucket.next;
                }
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
