package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = buckets[bucketIndex];
        Node<K, V> newNode = new Node<>(key, value);
        if (current == null) {// Jeśli nie ma jeszcze elementów w tym kubełku
            buckets[bucketIndex] = newNode;
            size++;
            return;
        }
        while (current.next != null) { //badanie czy są powtórki i podmiana value
            if (current.key == null ? key == null : current.key.equals(key)) { // Sprawdź, czy klucz już istnieje
                current.value = value; // Podmień wartość
                return;
            }
            current = current.next;
        }
        while (current.next != null) { // dodawanie nowej value
            current = current.next;
        }
        current.next = newNode;
        size++;

        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }

    }


    @Override
    public V getValue(K key) {
        Node<K, V> current = buckets[getBucketIndex(key)];

        while (current != null) {
            if (current.key == null && key == null) {
                return current.value;
            } else if (current.key != null && current.key.equals(key)) {
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
    }

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * GROW_FACTOR];
        size = 0;

        for (Node<K, V> node : oldBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
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
