package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LOAD = 0.75;
    private int size;
    private Node<K, V>[] table;
    private int threshold;
    private int maxSize;

    @Override
    public void put(K key, V value) {
        if (table == null || (size >= threshold)) {
            maxSize = resize();
        }
        int index = indexByHash(key);

        if (table[index] == null) {
            table[index] = createNode(key, value, null);
            size++;
            return;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                break;
            }
            node = node.next;
        }
        node.next = createNode(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        if (table[indexByHash(key)] != null) {
            Node<K, V> node = table[indexByHash(key)];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                } else {
                    node = node.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int resize() {
        if (table == null) {
            Node<K, V>[] newTab = (Node<K, V>[]) new Node[DEFAULT_SIZE];
            table = newTab;
            threshold = (int) (DEFAULT_SIZE * MAX_LOAD);
            maxSize = DEFAULT_SIZE;
            return DEFAULT_SIZE;
        } else {
            final Node<K, V>[] tempTable = table;
            table = (Node<K, V>[]) new Node[maxSize * 2];
            maxSize = maxSize * 2;
            threshold = (int) (maxSize * MAX_LOAD);
            size = 0;
            for (int i = 0; i < tempTable.length; i++) {
                if (tempTable[i] != null) {
                    if (tempTable[i].next == null) {
                        put(tempTable[i].key, tempTable[i].value);
                    } else {
                        Node<K, V> node = tempTable[i];
                        while (node != null) {
                            put(node.key, node.value);
                            node = node.next;
                        }
                    }
                }

            }
        }
        return maxSize;
    }

    private int indexByHash(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % maxSize);
    }

    private Node<K, V> createNode(K key, V value, Node<K, V> next) {
        return new Node<>(indexByHash(key), key, value, next);
    }

    class Node<K, V> {
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

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}

