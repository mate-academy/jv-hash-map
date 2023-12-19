package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MODIFIER = 2;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int bucket = getBucket(key);
        Node<K, V> currentNode = table[bucket];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                if (!Objects.equals(currentNode.value, value)) {
                    currentNode.value = value;
                }
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[bucket]);
        table[bucket] = newNode;
        size++;
        if (size == threshold) {
            increaseCapacity(table.length);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getBucket(key)];
        while (currentNode != null) {
            if (Objects.equals(key,currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void increaseCapacity(int oldCapacity) {
        size = 0;
        Node<K,V>[] oldTable = table;
        int newCapacity = oldCapacity * CAPACITY_MODIFIER;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[oldCapacity * CAPACITY_MODIFIER];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int result = 17;
        result += 31 * key.hashCode();
        return result;
    }

    private int getBucket(K key) {
        return Math.abs(hash(key)) % table.length;
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
