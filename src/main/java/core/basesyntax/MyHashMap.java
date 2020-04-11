package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double FILL_FACTOR = 0.75;

    private Node<K, V>[] buckets;
    private double max_fill;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        max_fill = DEFAULT_CAPACITY * FILL_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if( size == max_fill) {
            resize();
        }
        int index = getIndex(key, buckets.length);
        Node<K, V> node = getNode(key, index);
        if (node != null){
            node.value = value;
        } else {
            Node<K, V> entry = new Node<>(key, value, buckets[index]);
            buckets[index] = entry;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key, getIndex(key, buckets.length));
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key, int capacity) {
        return Math.abs(key.hashCode() % capacity);
    }

    private int getIndex(K key, int capacity) {
        return key == null ? 0 : hash(key, capacity);
    }

    private Node getNode(K key, int index) {
        Node<K, V> node = buckets[index];
        while (node != null){
            if(key == node.key || key != null && key.equals(node.key)){
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] newBucketsSet = new Node[buckets.length << 1];
        max_fill = newBucketsSet.length * FILL_FACTOR;
        size = 0;
        Node<K, V>[] deadBuckets = buckets;
        buckets = newBucketsSet;
        for (Node<K,V> bucket: deadBuckets) {
            while (bucket != null){
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
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
