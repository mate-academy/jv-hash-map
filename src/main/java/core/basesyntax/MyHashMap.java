package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] buckets;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (isKeysEqual(key, current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key == null ? 0 : key.hashCode(), key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (isKeysEqual(key, current.key)) {
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

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * LOAD_FACTOR);

        Node<K, V>[] newBuckets = new Node[capacity];

        for (int i = 0; i < buckets.length; i++) {
            Node<K, V> current = buckets[i];
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = Math.abs(current.hash % capacity);
                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;
                current = next;
            }
        }
        buckets = newBuckets;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private boolean isKeysEqual(K key1, K key2) {
        if (key1 == null && key2 == null) {
            return true;
        }
        if (key1 == null || key2 == null) {
            return false;
        }
        return key1.equals(key2);
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
}
