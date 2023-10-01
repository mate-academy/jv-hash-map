package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

        Node<K, V> node = new Node<>(key, value, null);

        int hash = getHash(key);

        Node<K, V> nodeByHash = table[hash];

        if (nodeByHash == null) {
            table[hash] = node;
        } else {
            Node<K, V> curr = nodeByHash;
            Node<K, V> next;

            do {
                next = curr.next;

                if (Objects.equals(curr.key, key)) {
                    curr.value = node.value;
                    return;
                }

                if (next == null) {
                    curr.next = node;
                }

                curr = next;
            } while (next != null);
        }

        size++;
        if (size > (table.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getHash(key)];

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

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        size = 0;
        transferAllData(oldTable);
    }

    private void transferAllData(Node<K, V>[] src) {
        Node<K, V> next;
        for (Node<K, V> node : src) {
            while (node != null) {
                next = node.next;
                node.next = null;
                put(node.key, node.value);
                node = next;
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
