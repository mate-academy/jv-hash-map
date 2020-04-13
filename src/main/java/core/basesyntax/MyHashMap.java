package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    int size;
    private double threshold;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        size = 0;
        threshold = DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR;
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> bucket = buckets[getIndex(key)];
        Node<K, V> prevNode;
        if (bucket == null) {
            buckets[getIndex(key)] = new Node<>(key, value, null);
            size++;
        } else {
            do {
                if (compareKeys(bucket.key, key)) {
                    bucket.value = value;
                    return;
                }
                prevNode = bucket;
                bucket = bucket.next;
            } while (bucket != null);
            prevNode.next = new Node(key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = buckets[getIndex(key)];
        while (bucket != null) {
            if (compareKeys(bucket.key, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node next;

        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K, V>[] resizedBuckets = new Node[buckets.length * 2];
        threshold = resizedBuckets.length * DEFAULT_LOAD_FACTOR;
        size = 0;
        Node<K, V>[] old = buckets;
        buckets = resizedBuckets;
        for (Node<K, V> bucket : old) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs((key.hashCode() % buckets.length));
    }

    private boolean compareKeys(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }
}
