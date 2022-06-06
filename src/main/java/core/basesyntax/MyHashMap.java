package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int bucketIndex = getBucketIndex(key);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> curentNode = table[bucketIndex];
            while (curentNode != null) {
                if (Objects.equals(key, curentNode.key)) {
                    curentNode.value = value;
                    return;
                }
                if (curentNode.next == null) {
                    curentNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                curentNode = curentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketIndex(key)];
        while (node != null) {
            if (Objects.equals(key,node.key)) {
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

    private int getHash(K key) {
        int hashCode;
        return (key == null) ? 0 : (hashCode = key.hashCode()) ^ (hashCode >>> 16);
    }

    private int getBucketIndex(K key) {
        return Math.abs(getHash(key) % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        threshold = threshold << 1;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
