package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node<?, ?>[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = hash(key);
        int index = getIndex(hash);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)
                    || key == null && currentNode.key == null) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(hash, key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }

        currentNode = table[index];
        table[index] = new Node<>(hash, key, value, currentNode);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int hash = hash(key);
        int index = getIndex(hash);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private int getIndex(int hash) {
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash % table.length;
    }

    private void resize() {
        if (size == table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            table = (Node<K,V>[])new Node[table.length * GROW_FACTOR];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
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
