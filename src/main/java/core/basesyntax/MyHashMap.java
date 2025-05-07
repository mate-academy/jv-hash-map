package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_CAPACITY = 16;
    private static double LOAD_FACTOR = 0.75;
    private static int DOUBLE_BUCKET = 2;
    private int size;
    private Node<K,V>[] bucket;
    private int threshold;

    public MyHashMap() {
        this.bucket = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNedeed();
        int index = getIndex(key);
        Node<K, V> node = bucket[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNewNode(index, new Node<>(key, value));
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = bucket[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private int getIndex(K key) {
        return getHash(key) % bucket.length;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resizeIfNedeed() {
        if (size >= threshold) {
            Node<K,V>[] prevTable = bucket;
            bucket = new Node[prevTable.length * DOUBLE_BUCKET];
            size = 0;
            threshold = (int) (bucket.length * LOAD_FACTOR);
            for (Node<K,V> curr: prevTable) {
                while (curr != null) {
                    put(curr.key, curr.value);
                    curr = curr.next;
                }
            }
        }
    }

    private void addNewNode(int index, Node<K,V> node) {
        node.next = bucket[index % bucket.length];
        bucket[index % bucket.length] = node;
        size++;
    }

    public class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
