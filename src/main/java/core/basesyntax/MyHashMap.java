package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndexForBucket(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            ++size;
            return;
        }
        putInFullBucket(index, key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndexForBucket(key)];
        while (node != null) {
            if (isEqual(key, node.key)) {
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

    private boolean isEqual(K firstKey, K secondKey) {
        return firstKey == secondKey || (firstKey != null && firstKey.equals(secondKey));
    }

    private int getIndexForBucket(K key) {
        return Math.abs(Objects.hashCode(key)) % table.length;
    }

    private void putInFullBucket(int index, K key, V value) {
        Node<K, V> currentNode = table[index];
        while (currentNode.next != null || isEqual(key, currentNode.key)) {
            if (isEqual(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(key, value, null);
        ++size;

    }

    private void resize() {
        if (size == threshold) {
            size = 0;
            Node<K, V>[] oldTable = table;
            int oldCapacity = oldTable.length;
            threshold = threshold * 2;
            table = new Node[oldCapacity * 2];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
