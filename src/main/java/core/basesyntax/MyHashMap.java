package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_CAPACITY = 16;
    private static double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Node<K,V>[] bucket;
    private int threshold = 0;

    public MyHashMap() {
        this.bucket = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNedeed();
        int hash = getHash(key);
        int index = hash % bucket.length;
        Node<K, V> node = bucket[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        if (key == null) {
            addNewNode(0, new Node<>(hash, key, value));
            return;
        }
        addNewNode(index, new Node<>(hash, key, value));
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = hash % bucket.length;
        Node<K, V> node = bucket[index];
        if (key == null) {
            Node<K,V> node1 = bucket[0];
            while (node != null) {
                if (Objects.equals(key, node1.key)) {
                    return node1.value;
                }
                node1 = node1.next;
            }
        }
        while (node != null) {
            if (key != null && Objects.equals(key, node.key)) {
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resizeIfNedeed() {
        if (size >= threshold) {
            Node<K,V>[] prevTable = bucket;
            bucket = new Node[prevTable.length * 2];
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

        public Node(int hash, K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
