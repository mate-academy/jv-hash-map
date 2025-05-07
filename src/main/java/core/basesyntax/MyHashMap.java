package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K,V>[] buckets;

    public MyHashMap() {
        buckets = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            grow();
        }
        int keyHashCode = key == null ? 0 : key.hashCode();
        int index = getIndex(key);
        if (buckets[index] != null) {
            for (Node<K, V> node = buckets[index]; ; node = node.next) {
                if (key == node.key || key != null && key.equals(node.key)) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = new Node<>(keyHashCode, key, value, null);
                    size++;
                    break;
                }
            }
        } else {
            buckets[index] = new Node<>(keyHashCode, key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
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

    private void grow() {
        Node<K, V>[] copyArray = buckets;
        int newCapacity = buckets.length << 1;
        buckets = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> bucket : copyArray) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
        threshold = (int) LOAD_FACTOR * newCapacity;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
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
