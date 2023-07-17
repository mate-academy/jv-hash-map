package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int GROWTH_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int threshold;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this.buckets = new Node[INITIAL_CAPACITY];
        this.capacity = INITIAL_CAPACITY;
        this.threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> bucket = buckets[index];
        while (bucket != null) {
            if (keyEquals(bucket.key, key)) {
                bucket.value = value;
                return;
            }
            bucket = bucket.next;
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getValueForNullKey();
        }
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (keyEquals(node.key, key)) {
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

    private void putForNullKey(V value) {
        Node<K, V> node = buckets[0];
        while (node != null) {
            if (node.key == null) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(null, value, 0);
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
    }

    private void resize() {
        capacity = capacity * GROWTH_FACTOR;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] newBuckets = new Node[capacity];
        for (Node<K, V> node : buckets) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key);
                node.next = newBuckets[index];
                newBuckets[index] = node;
                node = next;
            }
        }
        buckets = newBuckets;
    }

    private V getValueForNullKey() {
        Node<K, V> bucket = buckets[0];
        while (bucket != null) {
            if (bucket.key == null) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : Math.abs(key.hashCode());
        return hash % capacity;
    }

    private boolean keyEquals(K key1, K key2) {
        return key1 == null ? key2 == null : key1.equals(key2);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
