package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final short GROWTH_FACTOR = 2;
    private int threshold = 12; //initial threshold value
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        putToTable(key, value, table, false);
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (key == node.key ||
                        node.key != null
                                && node.key.equals(key)) {
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

    private void resize() {
        capacity = capacity * GROWTH_FACTOR;
        threshold = threshold * GROWTH_FACTOR;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : table) { //Transfer
            while (node != null) {
                putToTable(node.key, node.value, newTable, true);
                node = node.next;
            }
        }
        table = newTable;
    }

    private void putToTable(K key, V vale, Node<K, V>[] table, boolean resize) {

    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void updateHash(){
        for (Node<K, V> node : table) {
            while (node != null) {
                node.hash = getHash(node.key);
            }
        }
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;
        private int hash;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
