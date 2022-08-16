package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(getIndex(key), key, value);
        if (table[node.hash] == null) {
            table[node.hash] = node;
            size++;
            return;
        }
        Node<K, V> newNode = table[node.hash];
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
                newNode.value = node.value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = node;
                size++;
                return;
            }
            newNode = newNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getIndex(key)];
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
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] oldTable = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K,V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
