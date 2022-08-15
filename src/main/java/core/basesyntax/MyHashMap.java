package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[capacity];
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> presentNode = table[bucketIndex];
        if (presentNode == null) {
            table[bucketIndex] = newNode;
            size++;
        } else {
            Node<K, V> lastNode = null;
            Node<K, V> equalNode = null;
            for (Node<K, V> node = presentNode; node != null; node = node.next) {
                if (Objects.equals(node.key, key)) {
                    equalNode = node;
                    node.value = newNode.value;
                }
                lastNode = node;
            }
            if (equalNode == null) {
                lastNode.next = newNode;
                size++;
            }
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V> getNode(K key) {
        for (Node<K, V> newNode : table) {
            if (newNode == null) {
                continue;
            }
            for (Node<K, V> node = newNode; node != null; node = node.next) {
                if (Objects.equals(node.key, key)) {
                    return node;
                }
            }
        }
        return null;
    }

    private void resize() {
        final Node<K,V>[] oldTable = table;
        if (size > threshold) {
            capacity = capacity * 2;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
            table = newTable;
            if (oldTable != null) {
                for (Node<K, V> element : oldTable) {
                    if (element != null) {
                        newTable[getBucketIndex(element.key)] = element;
                    }
                }
            }
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : (Math. abs(key.hashCode()) % capacity);
    }
}
