package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    public MyHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        if (buckets[getBucketIndex(key)] == null) {
            buckets[getBucketIndex(key)] = new Node<>(key, value);
            size++;
        } else {
            putIfCollision(key, value);
        }
    }

    private void putIfCollision(K key, V value) {
        Node<K, V> node = buckets[getBucketIndex(key)];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<K, V>(key, value);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = buckets[bucketIndex];

        while (current != null) {
            if (current.key == null ? current.key == key : current.key.equals(key)) {
                // Check if the key already exists
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

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
        //hash code calculation
    }

    private void resizeIfNeeded() {
        threshold = (int) (buckets.length * LOAD_FACTOR);
        if (size == threshold) {
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[oldBuckets.length * GROW_FACTOR];
            size = 0;
            //size zero to be able to increase it thanks to the put method
            for (Node<K, V> node : oldBuckets) {
                //rewriting the old board to the new one
                while (node != null) {
                    //buckets is an enlarged array and oldbuckets -> buckets
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
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
