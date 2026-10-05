package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        size = 0;
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

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key, buckets.length);
        Node<K, V> current = buckets[index];

        if (current == null) {
            buckets[index] = new Node<>(key, value);
            size++;
            return;
        }

        while (current != null) {
            if (keysAreEqual(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, buckets.length);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (keysAreEqual(current.key, key)) {
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
        if (size >= buckets.length * LOAD_FACTOR) {
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[oldBuckets.length * 2];
            size = 0;
            for (Node<K, V> node : oldBuckets) {
                Node<K, V> current = node;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private int getIndex(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode() % length);
    }

    private boolean keysAreEqual(K k1, K k2) {
        return k1 == k2 || (k1 != null && k1.equals(k2));
    }
}
