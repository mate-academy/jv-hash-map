package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int size;
    private int capacity = DEFAULT_CAPACITY;
    private Node<K, V>[] buckets = new Node[capacity];

    @Override
    public void put(K key, V value) {
        int hash = hashCode(key);
        if (buckets[hash] == null) {
            buckets[hash] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> node = buckets[hash];
            while (node != null) {
                if (keyEquals(node.key, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            buckets[hash] = new Node<>(hash, key, value, buckets[hash]);
            size++;
        }

        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hashCode(key);
        Node<K, V> node = buckets[hash];
        while (node != null) {
            if (keyEquals(node.key, key)) {
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

    private void resize() {
        capacity = capacity * GROW_FACTOR;
        Node<K, V>[] resizeBuckets = new Node[capacity];

        for (Node<K, V> node : buckets) {
            while (node != null) {
                int newHashCode = hashCode(node.key) % capacity;
                if (resizeBuckets[newHashCode] == null) {
                    resizeBuckets[newHashCode] = new Node<>(
                            newHashCode, node.key, node.value, null);
                } else {
                    Node<K, V> newNode = new Node<>(
                            newHashCode, node.key, node.value, resizeBuckets[newHashCode]);
                    resizeBuckets[newHashCode] = newNode;
                }
                node = node.next;
            }
        }
        buckets = resizeBuckets;
    }

    private boolean keyEquals(K k1, K k2) {
        if (k1 == null) {
            return k2 == null;
        }
        return k1.equals(k2);
    }

    private int hashCode(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    public static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

