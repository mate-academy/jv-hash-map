package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] buckets;
    private double threshold;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getBucket(key, buckets.length);
        Node<K, V> element = findNode(key, index);
        if (element != null) {
            element.value = value;
        } else {
            Node<K, V> entry = new Node<>(key, value, buckets[index]);
            buckets[index] = entry;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> element = findNode(key, getBucket(key, buckets.length));
        return element != null ? element.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newBuckets = new Node[buckets.length * 2];
        threshold = newBuckets.length * LOAD_FACTOR;
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

    private Node findNode(K key, int index) {
        Node<K, V> element = buckets[index];
        while (element != null) {
            if (key == element.key || key != null && key.equals(element.key)) {
                return element;
            }
            element = element.next;
        }
        return null;
    }

    private int getBucket(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
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
