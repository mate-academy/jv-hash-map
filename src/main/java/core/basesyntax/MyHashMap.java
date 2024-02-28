package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int keyHash = hash(key);
        addNode(key, value, keyHash);
    }

    @Override
    public V getValue(K key) {
        int keyHash = hash(key);
        int position = keyHash % table.length;
        Node node = table[position];
        if (node == null) {
            return null;
        }
        if (node.key == null) {
            return (V) node.value;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    return (V) node.value;
                }
                node = node.next;
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;

        private K key;

        private V value;

        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    private int hash(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return hash < 0 ? -hash : hash;
    }

    private void addNode(K key, V value, int keyHash) {
        int bucketNumber = keyHash % table.length;
        if (size == 0 || table[bucketNumber] == null) {
            ifFullResize(table);
            table[bucketNumber] = new Node<>(keyHash, key, value, null);
            size++;
            return;
        }
        addToBucketNextNode(key, value, keyHash, bucketNumber);
    }

    private void addToBucketNextNode(K key, V value, int keyHash, int bucketNumber) {
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
        node = new Node<>(keyHash, key, value, null);
        previousNode.next = node;
        size++;
        ifFullResize(table);
    }

    private void ifFullResize(Node<K,V>[] table) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] fullTable = table;
            table = (Node<K, V>[]) new Node[table.length * 2];
            transfer(fullTable);
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node == null) {
                continue;
            }
            if (node.next == null) {
                this.put(node.key, node.value);
                continue;
            }
            Node<K, V> tempNode = node;
            do {
                this.put(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            } while (tempNode != null);
        }
    }
}
