package core.basesyntax;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_ARRAY_INCREASE = 2;
    private static final float LOAD_FACTORY = 0.75f;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this.buckets = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        putInBucketArray(getHash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = (buckets == null) ? null : buckets[getIndex(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
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

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private void resize() {
        int newArraySize = buckets.length * DEFAULT_ARRAY_INCREASE;
        if ((float)size / buckets.length >= LOAD_FACTORY) {
            Node<K, V>[] oldBucketsArray = buckets;
            buckets = (Node<K, V>[]) new Node[newArraySize];
            size = 0;
            for (Node<K, V> node : oldBucketsArray) {
                while (node != null) {
                    putInBucketArray(node.hash, node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return ((key == null) ? 0 : Math.abs(key.hashCode())) % buckets.length;
    }

    private void putInBucketArray(int hash, K key, V value) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new Node<>(hash, key, value);
            size++;
            return;
        }
        Node<K, V> bucketNode = buckets[index];
        while (bucketNode != null) {
            if (key == bucketNode.key
                    || key != null && key.equals(bucketNode.key)) {
                bucketNode.value = value;
                return;
            }
            if (bucketNode.next == null) {
                bucketNode.next = new Node<>(hash, key, value);
                size++;
                return;
            }
            bucketNode = bucketNode.next;
        }
    }
}
