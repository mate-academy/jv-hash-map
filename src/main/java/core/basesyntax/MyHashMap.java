package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final int DEFAULT_COEFFICIENT = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_LENGTH];
        this.threshold = (int) (DEFAULT_LENGTH * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizing();
        int hashOfKey = getHashCode(key);
        int bucket = getIndex(key);
        Node<K, V> node = table[bucket];
        Node<K, V> newNode = new Node<>(hashOfKey, key, value, null);
        if (node == null) {
            table[bucket] = newNode;
        }
        while (node != null) {
            if (node.hash == hashOfKey && Objects.equals(key, node.key)) {
                node.value = value;
                return;
            } else if (node.next == null) {
                node.next = newNode;
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hashOfKey = getHashCode(key);
        int bucket = getIndex(key);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (node.hash == hashOfKey && Objects.equals(key, node.key)) {
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

    private int getHashCode(K key) {
        int hash;
        if (key == null) {
            return 0;
        }
        return (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int getIndex(K key) {
        int index = getHashCode(key) % (table.length - 1);
        if (index < 0) {
            index = -index;
        }
        return index;
    }

    private void resizing() {
        if (threshold == size) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * DEFAULT_COEFFICIENT];
            threshold = (int) (table.length * LOAD_FACTOR);
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
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
