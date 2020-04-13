package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private float threshold;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            reSize();
        }
        int index = getBucketNumber(key);
        Node<K, V> node = findNode(key, index);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> newBucketNode = new Node<>(key, value, buckets[index]);
            buckets[index] = newBucketNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key, getBucketNumber(key));
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node findNode(K key, int index) {
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void reSize() {
        Node<K, V>[] newBuckets = new Node[buckets.length * 2];
        threshold = newBuckets.length * DEFAULT_LOAD_FACTOR;
        size = 0;
        Node<K, V>[] oldBuckets = buckets;
        buckets = newBuckets;
        for (Node<K, V> bucket : oldBuckets) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int getBucketNumber(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
