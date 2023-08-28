package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = (Node<K,V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }

        Node<K, V> node = getNode(key);

        if (node != null) {
            node.value = value;
        } else {
            int bucketIndex = getBucketIndex(key);
            int hashCode = (key == null) ? (0) : (Math.abs(key.hashCode()));
            Node<K, V> newNode = new Node<>(hashCode, key, value, table[bucketIndex]);
            table[bucketIndex] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);

        return (node == null) ? (null) : (node.value);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = (Node<K,V>[]) new Node[newCapacity];
        Node<K, V>[] oldTable = table;
        table = newTable;
        size = 0;

        for (Node<K, V> bucket : oldTable) {
            Node<K, V> node = bucket;

            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K, V> getNode(K key) {
        if (size == 0) {
            return null;
        }

        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = table[bucketIndex];

        while (node != null) {
            if ((node.key == null) ? (key == null) : (node.key.equals(key))) {
                return node;
            }
            node = node.next;
        }

        return null;
    }

    private int getBucketIndex(K key) {
        return (key == null) ? (0) : (Math.abs(key.hashCode()) % table.length);
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
