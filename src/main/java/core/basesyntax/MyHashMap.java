package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_SIZE = 16;
    private static int DEFAULT_INCREASE = 2;
    private static float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int currentLoad;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
        currentLoad = (int) (DEFAULT_SIZE * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(null, hash(key), value, key);
        }
        while (node != null) {
            if (node.hash == hash(key)
                    && (Objects.equals(key, node.key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(null, hash(key), value, key);
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash(key)
                    && (Objects.equals(key, node.key))) {
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

    private static class Node<K, V> {
        private Node<K, V> next;
        private final int hash;
        private V value;
        private final K key;

        Node(Node<K, V> next, int hash, V value, K key) {
            this.next = next;
            this.hash = hash;
            this.value = value;
            this.key = key;

        }

        @Override
        public final String toString() {
            return key + "=" + value;
        }
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int getIndex(K key) {
        return hash(key) & (table.length - 1);
    }

    private void resize() {
        if (currentLoad == size) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[table.length * DEFAULT_INCREASE];
            currentLoad *= DEFAULT_INCREASE;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}

