package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = key == null ? 0 : getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (buckets[index] == null) {
            buckets[index] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = buckets[index];
            while (currentNode != null) {
                if (equalsByKey(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            newNode.next = buckets[index];
            buckets[index] = newNode;
            size++;
        }

        if ((float) size / buckets.length >= LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : getIndex(key);
        Node<K, V> currentNode = buckets[index];

        while (currentNode != null) {
            if (equalsByKey(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private boolean equalsByKey(K key1, K key2) {
        return key1 == null ? key2 == null : key1.equals(key2);
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];

        for (Node<K, V> oldNode : buckets) {
            while (oldNode != null) {
                int newIndex = Math.abs(oldNode.key.hashCode()) % newCapacity;
                Node<K, V> newNode = new Node<>(oldNode.key, oldNode.value);
                newNode.next = newBuckets[newIndex];
                newBuckets[newIndex] = newNode;
                oldNode = oldNode.next;
            }
        }

        buckets = newBuckets;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
