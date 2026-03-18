package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity = DEFAULT_CAPACITY;
    private static final double LOADFACTOR = 0.75;
    private Node<K, V>[] buckets = (Node<K, V>[]) new Node[capacity];

    @Override
    public void put(K key, V value) {
        if ((double) size / capacity >= LOADFACTOR) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> node = buckets[index];

        while (node != null) {
            if (key == null ? node.key == null : key.equals(node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];

        while (node != null) {
            if (key == null ? node.key == null : key.equals(node.key)) {
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

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        capacity *= 2;
        buckets = (Node<K, V>[]) new Node[capacity];
        size = 0;

        for (int i = 0; i < oldBuckets.length; i++) {
            Node<K, V> node = oldBuckets[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }
}
