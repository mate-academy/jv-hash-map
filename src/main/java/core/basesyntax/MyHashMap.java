package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private boolean isResizing = false;

    private Node<K, V>[] buckets;
    private int size;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (key == null ? current.key == null : key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(key, value, buckets[index]);
        buckets[index] = newNode;
        size++;
        if (!isResizing && (float) size / buckets.length >= LOAD_FACTOR) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        isResizing = true; // Увімкнути прапорець, щоб уникнути рекурсії
        Node<K, V>[] oldBuckets = buckets;
        buckets = (Node<K, V>[]) new Node[oldBuckets.length * 2];
        size = 0;

        for (Node<K, V> node : oldBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }

        isResizing = false;
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (key == null ? current.key == null : key.equals(current.key)) {
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
