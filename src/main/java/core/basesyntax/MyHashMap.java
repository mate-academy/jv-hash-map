package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_SIZE = 1 << 4;
    private static int DEFAULT_INCREASE = 2;
    private static float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int currentLoad;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
        currentLoad = DEFAULT_SIZE * (int) DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(null, getHash(key), value, key);
        }
        while (node != null) {
            if (node.hash == getHash(key)
                    && (Objects.equals(key, node.key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(null, getHash(key), value, key);
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
            if (node.hash == getHash(key)
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

    private int getHash(K key) {
        int h;
        return (key == null) ? 0 : key.hashCode() ^ key.hashCode() >>> 16;
    }

    private int getIndex(K key) {
        return getHash(key) & (table.length - 1);
    }
}
