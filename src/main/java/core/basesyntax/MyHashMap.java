package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER = 2;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] nodeTable;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        nodeTable = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        putValue(key, value, getKeyHash(key));
    }

    @Override
    public V getValue(K key) {
        if (!isEmpty()) {
            int index = calculateBucketIndex(getKeyHash(key));
            Node<K, V> currentNode = nodeTable[index];
            while (currentNode != null) {
                if (isNodeEquals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int getKeyHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int calculateBucketIndex(int hash) {
        return Math.abs(hash % capacity);
    }

    private void resize() {
        capacity = capacity * MULTIPLIER;
        threshold = threshold * MULTIPLIER;
        transportNodes();
    }

    private void transportNodes() {
        Node<K, V>[] oldNodeTable = nodeTable;
        nodeTable = new Node[capacity];
        size = 0;
        Node<K, V> currentNode;
        for (Node<K, V> node : oldNodeTable) {
            currentNode = node;
            while (currentNode != null) {
                putValue(currentNode.key, currentNode.value, currentNode.hash);
                currentNode = currentNode.next;
            }
        }
    }

    private void putValue(K key, V value, int hash) {
        int index = calculateBucketIndex(hash);
        Node<K, V> currentNode = nodeTable[index];
        while (currentNode != null) {
            if (isNodeEquals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(hash, key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        nodeTable[index] = new Node<>(hash, key, value, null);
        size++;
    }

    private boolean isNodeEquals(K nodeKey, K key) {
        return nodeKey == key || (nodeKey != null && nodeKey.equals(key));
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
}
