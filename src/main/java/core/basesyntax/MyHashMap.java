package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private float loadFactor;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, LOAD_FACTOR);
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.size = 0;
        this.buckets = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int index = key == null ? 0 : getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> existingNode = buckets[index];
        if (existingNode == null) {
            buckets[index] = newNode;
            size++;

        } else {
            while (existingNode != null) {
                if ((key == null && existingNode.key == null) || (key != null && key
                        .equals(existingNode.key))) {
                    existingNode.value = value;
                    return;
                }
                existingNode = existingNode.next;
            }
            newNode.next = buckets[index];
            buckets[index] = newNode;
            size++;
            if ((double) size / capacity > loadFactor) {
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
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
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] newBuckets = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : buckets) {
            while (node != null) {
                int newIndex = getIndex(node.key);
                Node<K, V> next = node.next;
                node.next = newBuckets[newIndex];
                newBuckets[newIndex] = node;
                node = next;
            }
        }
        buckets = newBuckets;
    }
}
