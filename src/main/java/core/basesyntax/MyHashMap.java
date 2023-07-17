package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_MULTIPLIER = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private int changeableCapacity = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }


    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        checkSize();
        int index = getIndex(newNode);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> nodeInBucket = table[index];
            do {
                if (Objects.equals(nodeInBucket.key, key)) {
                    nodeInBucket.value = value;
                    return;
                }
                if (nodeInBucket.next == null) {
                    nodeInBucket.next = newNode;
                    break;
                }
                nodeInBucket = nodeInBucket.next;
            } while (nodeInBucket != null);

        }
        size++;
    }

    private void checkSize() {
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        changeableCapacity *= DEFAULT_RESIZE_MULTIPLIER;
        Node<K, V>[] newTable = new Node[changeableCapacity];
        table = newTable;
        threshold = (int) (LOAD_FACTOR * changeableCapacity);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfKey = key == null ? 0 : (Math.abs(key.hashCode()) % 16);
        Node<K, V> nodeInTable = table[indexOfKey];
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(node.key, key)){
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(Node<K, V> node) {
        return node.hash % changeableCapacity;
    }

    private class Node<K, V> {
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
