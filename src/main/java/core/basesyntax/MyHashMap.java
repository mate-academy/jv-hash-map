package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;
    private Node<K,V>[] table;

    @Override
    public void put(K key, V value) {
        if (this.table == null || (this.size + 1) > this.table.length * LOAD_FACTOR) {
            resize();
        }

        Node<K, V> node = getNode(key);

        if (node != null) {
            node.value = value;
        } else {
            int bucketNumber = getBucketNumber(key);
            int hashCode = (key == null) ? (0) : (Math.abs(key.hashCode()));
            Node<K, V> newNode = new Node<>(hashCode, key, value, this.table[bucketNumber]);
            this.table[bucketNumber] = newNode;
            this.size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);

        return (node == null) ? (null) : (node.value);
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private void resize() {
        if (this.table == null) {
            this.table = (Node<K,V>[]) new Node[INITIAL_CAPACITY];
            return;
        }

        int newCapacity = this.table.length * 2;
        Node<K, V>[] newTable = (Node<K,V>[]) new Node[newCapacity];
        Node<K, V>[] oldTable = table;
        this.table = newTable;
        this.size = 0;

        for (Node<K, V> bucket : oldTable) {
            Node<K, V> node = bucket;

            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K, V> getNode(K key) {
        if (this.size == 0) {
            return null;
        }

        int bucketNumber = getBucketNumber(key);
        Node<K, V> node = this.table[bucketNumber];

        while (node != null) {
            if ((node.key == null) ? (key == null) : (node.key.equals(key))) {
                return node;
            }
            node = node.next;
        }

        return null;
    }

    private int getBucketNumber(K key) {
        return (key == null) ? (0) : (Math.abs(key.hashCode()) % this.table.length);
    }

    private class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
