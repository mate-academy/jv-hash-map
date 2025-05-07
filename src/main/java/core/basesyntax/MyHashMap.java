package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getBucketIndexOnHash(key);
        Node<K, V> lastInChain = new Node<>(key, value, null);
        if (table[index] != null) {
            Node<K, V> currentNode = table[index];
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
            }
            currentNode.next = lastInChain;
            size++;
            return;
        }
        table[index] = lastInChain;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getBucketIndexOnHash(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndexOnHash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode() % DEFAULT_INITIAL_CAPACITY));
    }

    private int getResizeThreshold() {
        return (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private void resize() {
        if (size == getResizeThreshold()) {
            Node<K, V>[] toRedistribute = table;
            size = 0;
            table = new Node[DEFAULT_INITIAL_CAPACITY << 1];
            for (Node<K, V> node : toRedistribute) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
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
