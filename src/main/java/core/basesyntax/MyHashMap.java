package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K,V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K,V> node = table[index];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
