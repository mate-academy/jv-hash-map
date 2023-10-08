package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int index = hashIndex(key);
        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
        if (buckets[index] == null) {
            buckets[index] = newNode;
            size++;
        } else {
            Node<K, V> nodeNew = buckets[index];
            while (nodeNew != null) {
                if (Objects.equals(newNode.key, nodeNew.key)) {
                    nodeNew.value = value;
                    return;
                }
                if (nodeNew.next == null) {
                    nodeNew.next = newNode;
                    size++;
                    return;
                }
                nodeNew = nodeNew.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = buckets[hashIndex(key)];
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hashIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % buckets.length);
    }

    public void resize() {
        size = 0;
        Node<K, V>[] oldbuckets = buckets;
        buckets = new Node[buckets.length * 2];
        for (Node<K, V> node : oldbuckets) {
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

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
