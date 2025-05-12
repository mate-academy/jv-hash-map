package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
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

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V value) {
            this.value = value;
        }

        private Node<K, V> getNext() {
            return next;
        }

        private void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= buckets.length * LOAD_FACTOR) {
            resize();
        }
        int index = getBucketIndex(key);
        Node<K, V> node = buckets[index];

        if (node == null) {
            buckets[index] = new Node<>(key, value);
            size++;
            return;
        }

        while (node != null) {
            if ((key == null && node.getKey() == null)
                    || (key != null && key.equals(node.getKey()))) {
                node.setValue(value);
                return;
            }
            if (node.getNext() == null) {
                node.setNext(new Node<>(key, value));
                size++;
                return;
            }
            node = node.getNext();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        buckets = (Node<K, V>[]) new Node[oldBuckets.length * 2];
        size = 0;

        for (Node<K, V> node : oldBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % buckets.length);
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> node = buckets[index];

        while (node != null) {
            if ((key == null && node.getKey() == null)
                    || (key != null && key.equals(node.getKey()))) {
                return node.getValue();
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
