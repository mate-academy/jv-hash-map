package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private final float loadFactor;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        this.buckets = new Node[initialCapacity];
        this.size = 0;
        this.loadFactor = loadFactor;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        Node<K, V> current = buckets[bucketIndex];
        Node<K, V> previous = null;

        while (current != null) {
            if (key == null && current.key == null
                    || (key != null && key.equals(current.key))) {
                current.value = value;
                return;
            }
            previous = current;
            current = current.next;
        }
        if (previous == null) {
            buckets[bucketIndex] = newNode;
        } else {
            previous.next = newNode;
        }
        size++;
        if (size > buckets.length * loadFactor) {
            resizeMap();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = buckets[bucketIndex];

        while (current != null) {
            if (key == null && current.key == null || key != null && key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void resizeMap() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];

        for (Node<K, V> bucket : buckets) {
            while (bucket != null) {
                Node<K, V> current = bucket;
                bucket = bucket.next;
                int newIndex = Math.abs(current.key.hashCode()) % newCapacity;
                current.next = null;

                if (newBuckets[newIndex] == null) {
                    newBuckets[newIndex] = current;
                } else {
                    Node<K, V> lastNode = newBuckets[newIndex];
                    while (lastNode.next != null) {
                        lastNode = lastNode.next;
                    }
                    lastNode.next = current;
                }
            }
        }

        buckets = newBuckets;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
