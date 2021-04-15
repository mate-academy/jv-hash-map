package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int indexBucket;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            size = 0;
            reSize();
        }
        indexBucket = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> nodeInTable = table[indexBucket];
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
            table[indexBucket] = newNode;
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
        table = new Node[table.length * RESIZE_COEFFICIENT];

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
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public static class Node<K, V> {

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
