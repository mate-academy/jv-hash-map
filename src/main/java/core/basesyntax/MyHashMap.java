package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] buckets;

    private int size;
    private int capacity = DEFAULT_CAPACITY;
    private double threshold = capacity * LOAD_FACTOR;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (buckets[0] == null) {
                buckets[0] = new Node<>(null, value);
            } else {
                Node<K, V> current = buckets[0];
                while (current != null) {
                    if (current.key == null) {
                        current.value = value;
                        return;
                    }
                    if (current.next == null) {
                        break;
                    }
                    current = current.next;
                }
                current.next = new Node<>(null, value);
            }
            size++;

            if ((double) size / buckets.length > LOAD_FACTOR) {
                resize();
            }
            return;
        }

        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> current = buckets[index];
            while (current != null) {
                if (current.key != null && current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = newNode;
        }
        if (size >= threshold) {
            resize();
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (current.key != null && current.key.equals(key)) {
                return current.value;
            }
            if (current.key == null && key == null) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        return Math.abs(hash) % capacity;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];
        threshold = newCapacity * LOAD_FACTOR;
        capacity = newCapacity;

        for (Node<K, V> node : buckets) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key);
                node.next = newBuckets[index];
                newBuckets[index] = node;
                node = next;
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
            this.next = null;
        }
    }

}
