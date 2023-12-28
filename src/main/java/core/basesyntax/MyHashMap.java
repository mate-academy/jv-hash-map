package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private int threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] buckets = new Node[INITIAL_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        resize();
        int index = getNodeIndex(key);
        Node<K, V> node = buckets[index];

        if (node == null) {
            buckets[index] = new Node<>(key, value);
            size++;
        } else {
            addNode(node, key, value);
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : buckets) {
            Node<K, V> current = node;
            while (current != null) {
                if (keysEqual(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > threshold) {
            increase();
        }
    }

    private void addNode(Node<K, V> node, K key, V value) {
        while (true) {
            if (keysEqual(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value);
                size++;
                return;
            }
            node = node.next;
        }
    }

    private boolean keysEqual(K key1, K key2) {
        return (key1 == null && key2 == null) || (key1 != null && key1.equals(key2));
    }

    private void increase() {
        int newCapacity = buckets.length * MULTIPLIER;
        final Node<K, V>[] oldTable = buckets;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        buckets = new Node[newCapacity];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getNodeIndex(K key) {
        return (key == null) ? 0 : Math.abs((key.hashCode() % buckets.length));
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
