package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (key == null ? 0 : key.hashCode());
            return result;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (capacity * LOAD_FACTOR);
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            size = 0;
            reSize();
        }
        Node<K, V> newNode = new Node<>(hashKey(key), key, value, null);
        Node<K, V> nodeInTable = table[getIndex(key)];
        if (nodeInTable != null) {
            while (nodeInTable.next != null || Objects.equals(key, nodeInTable.key)) {
                if (Objects.equals(key, nodeInTable.key)) {
                    nodeInTable.value = value;
                    return;
                }
                nodeInTable = nodeInTable.next;
            }
            nodeInTable.next = newNode;
        } else {
            table[getIndex(key)] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = table[getIndex(key)];
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

    private Node<K, V>[] reSize() {

        threshold = threshold * 2;
        final Node<K, V>[] oldTable = table;
        table = new Node[capacity * RESIZE_COEFFICIENT];
        capacity = capacity * 2;
        for (Node<K, V> current : oldTable) {
            Node<K, V> node = current;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private int hashKey(K key) {
        if (key == null) {
            return 0;
        }
        int result = 17;
        result = 31 * result + key.hashCode();
        return result;
    }
}
