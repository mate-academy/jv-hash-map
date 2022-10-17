package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_INCREASE_FACTOR = 2;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    static class Node<K, V> {
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

    public MyHashMap() {
        buckets = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private int getKeyHash(K key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int getBucketIndex(K key) {
        int index = getKeyHash(key) % buckets.length;
        return index < 0 ? index * (-1) : index;
    }

    private Node<K, V>[] resize() {
        if (size >= threshold) {
            size = 0;
            Node<K, V>[] oldBuckets = buckets;
            buckets = (Node<K, V>[]) new Node[oldBuckets.length * CAPACITY_INCREASE_FACTOR];
            threshold = (int) (buckets.length * LOAD_FACTOR);
            for (Node<K, V> node : oldBuckets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
        return buckets;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int bucketIndex = getBucketIndex(key);
        Node<K, V> newNode = new Node<K, V>(getKeyHash(key), key, value, null);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = newNode;
            size++;
            return;
        }
        Node<K, V> current = buckets[bucketIndex];
        while (current != null) {
            if (current.key == key || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = newNode;
                size++;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = buckets[bucketIndex];
        while (current != null) {
            if (current.key == key || key != null && key.equals(current.key)) {
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
}
