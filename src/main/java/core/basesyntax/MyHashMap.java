package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    private int size;

    public static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int i = Math.abs(hash(key) % table.length);
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (table[i] == null) {
            table[i] = newNode;
            size++;
            return;
        }
        Node<K, V> node = table[i];
        while (node != null) {
            newNode = new Node<>(hash(key), key, value, null);
            if (node.key == key || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        int i = Math.abs(hash(key) % table.length);
        Node<K, V> node;
        node = table[i];
        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
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

    public int hash(K key) {
        return key == null ? 0 : (key.hashCode()) ^ (key.hashCode() >>> 16);
    }

    public Node<K, V>[] resize() {
        int oldCap = table.length;
        int oldThr = threshold;
        int oldSize = size;
        int newCap;
        int newThr;
        newCap = oldCap << 1;
        newThr = oldThr << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCap];
        Node<K, V>[] tempTable;
        tempTable = table;
        table = newTable;
        threshold = newThr;
        for (Node<K, V> nodes : tempTable) {
            if (nodes != null) {
                while (nodes != null) {
                    put(nodes.key, nodes.value);
                    size = oldSize;
                    nodes = nodes.next;
                }
            }
        }
        return table;
    }
}
