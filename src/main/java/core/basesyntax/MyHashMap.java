package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final float THRESHOLD_MULTIPLIER = 0.75F;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
        threshold = (int) (DEFAULT_SIZE * THRESHOLD_MULTIPLIER);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            grow();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(getHash(key), key, value, null);
            size++;
        } else {
            chainWalkthrough(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    private int getIndex(K key) {
        return getHash(key) % table.length;
    }

    private int getHash(K key) {
        return key != null ? Math.abs(key.hashCode()) : 0;
    }

    private void grow() {
        Node<K, V>[] oldNodeArray = table;
        table = new Node[table.length * 2];
        size = 0;
        transferToNewArray(oldNodeArray);
        threshold = (int) (table.length * THRESHOLD_MULTIPLIER);
    }

    private void transferToNewArray(Node<K, V>[] oldNodeArray) {
        for (Node<K, V> node : oldNodeArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void chainWalkthrough(K key, V value, int index) {
        Node<K, V> currentNode = table[index];
        while (currentNode.next != null) {
            if (Objects.equals(currentNode.key, key)) {
                break;
            }
            currentNode = currentNode.next;
        }
        if (Objects.equals(currentNode.key, key)) {
            currentNode.value = value;
        } else {
            currentNode.next = new Node<>(getHash(key), key, value, null);
            size++;
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
