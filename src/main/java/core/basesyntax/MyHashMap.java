package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_SIZE = 2;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[INITIAL_DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndex(key);
        if (size > buckets.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        if (buckets[index] == null) {
            buckets[index] = node;
            size++;
        } else {
            Node<K, V> newNode = buckets[index];
            while (newNode != null) {
                if (Objects.equals(newNode.key, node.key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    newNode.next = node;
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = buckets[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[buckets.length * INCREASE_SIZE];
        for (Node<K, V> node : oldBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
