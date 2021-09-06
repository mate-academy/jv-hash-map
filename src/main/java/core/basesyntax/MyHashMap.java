package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private final int capacity;
    private final int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = table.length;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = new Node<>(bucketIndex, key, value, null);
        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (isKeysEquals(bucketIndex, currentNode.hash, key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[bucketIndex] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (isKeysEquals(bucketIndex, node.hash, key, node.key)) {
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

    private boolean isKeysEquals(int keyHash, int nodeKeyHash, K key, K nodeKey) {
        return keyHash == nodeKeyHash
                && (key == nodeKey || (key != null && key.equals(nodeKey)));
    }

    private int supplementalHash(int hash) {
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        return hash ^ (hash >>> 7) ^ (hash >>> 4);
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : supplementalHash(key.hashCode()) & (capacity - 1);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity * RESIZE_FACTOR];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] node) {
        for (Node<K, V> currentNode : node) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
