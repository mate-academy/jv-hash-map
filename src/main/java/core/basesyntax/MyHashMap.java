package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int INCREASE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        putValue(newNode);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (Objects.equals(node.kay, key)) {
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
        if (size > table.length * LOAD_FACTOR) {
            Node<K, V>[] oldBuckets = table;
            table = new Node[oldBuckets.length * INCREASE_COEFFICIENT];
            Node<K, V> node;
            for (int i = 0; i < oldBuckets.length; i++) {
                node = oldBuckets[i];
                while (node != null && node.next != null) {
                    putValue(new Node<>(hash(node.kay), node.kay, node.value, null));
                    size--;
                    node = node.next;
                }
                if (node != null) {
                    putValue(new Node<>(hash(node.kay), node.kay, node.value, null));
                    size--;
                }
            }
        }
    }

    private int hash(K kay) {
        return Math.abs(kay == null ? 0 : (kay.hashCode() % table.length));
    }

    private void putValue(Node<K, V> newNode) {
        Node<K, V> node = table[newNode.hashCod];
        while (node != null) {
            if (Objects.equals(node.kay, newNode.kay)) {
                node.value = newNode.value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
        table[newNode.hashCod] = newNode;
        size++;
    }

    private class Node<K, V> {
        private int hashCod;
        private K kay;
        private V value;
        private Node<K, V> next;

        public Node(int hashCod, K kay, V value, Node<K, V> next) {
            this.hashCod = hashCod;
            this.kay = kay;
            this.value = value;
            this.next = next;
        }
    }
}
