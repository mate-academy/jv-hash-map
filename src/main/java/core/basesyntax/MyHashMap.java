package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int size = 0;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        buckets = new Node[capacity];
    }

    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int index = getBucketIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> current = buckets[index];
            while (current != null) {
                if (key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }

        size++;

        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
    }

    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }

        int index = getBucketIndex(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];

        for (Node<K, V> bucket : buckets) {
            while (bucket != null) {
                Node<K, V> next = bucket.next;
                int newIndex = Math.abs(bucket.key.hashCode() % newCapacity);

                bucket.next = newBuckets[newIndex];
                newBuckets[newIndex] = bucket;

                bucket = next;
            }
        }

        buckets = newBuckets;
        capacity = newCapacity;
    }

    private void putForNullKey(V value) {
        Node<K, V> current = buckets[0];
        while (current != null) {
            if (current.key == null) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(null, value, null);
                size++;
                return;
            }
            current = current.next;
        }
        buckets[0] = new Node<>(null, value, null);
        size++;
    }

    private V getForNullKey() {
        Node<K, V> current = buckets[0];
        while (current != null) {
            if (current.key == null) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
