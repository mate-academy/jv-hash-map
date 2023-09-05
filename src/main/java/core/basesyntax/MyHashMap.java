package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static int MULTYPLYCATION = 2;
    private static double LOAD_FACTOR = 0.75;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        increaseCapacity();
        int index = getIndex(key);
        Node<K, V> node = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (node == null) {
            table[index] = newNode;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            newNode.next = table[index];
            table[index] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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
        return hashCode(key) % table.length;
    }

    private void increaseCapacity() {
        if (size >= threshold) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * MULTYPLYCATION];
            threshold = (int) (table.length * LOAD_FACTOR);
            for (Node<K, V> singleNode : oldTable) {
                while (singleNode != null) {
                    put(singleNode.key, singleNode.value);
                    singleNode = singleNode.next;
                }
            }
        }
    }

    private int hashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
