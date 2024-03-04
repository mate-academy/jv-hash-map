package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

        int keyHash = hash(key);
        int bucketNumber = getIndex(keyHash);
        ifFullResize();
        if (table[bucketNumber] == null) {
            table[bucketNumber] = new Node<>(keyHash, key, value, null);
            size++;
            return;
        }
        Node node = table[bucketNumber];
        Node previousNode = null;

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            previousNode = node;
            node = node.next;
        }
        size++;
        node = new Node<>(keyHash, key, value, null);
        previousNode.next = node;
    }

    @Override
    public V getValue(K key) {
        int position = getIndex(hash(key));
        Node node = table[position];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }

    private int getIndex(int keyHash) {
        return keyHash % table.length;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;

        private V value;

        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return hash < 0 ? -hash : hash;
    }

    private void ifFullResize() {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[table.length * GROW_FACTOR];
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> tempNode = node;
            while (tempNode != null) {
                put(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            }
        }
    }
}
