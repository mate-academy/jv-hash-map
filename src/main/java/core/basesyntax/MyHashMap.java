package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_VALUE = 2;

    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            initialize(DEFAULT_INITIAL_CAPACITY);
        }
        boolean keyPresent = updateIfKeyPresent(key, value);
        if (!keyPresent) {
            if (size >= threshold) {
                resize();
            }
            createNewNode(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int bucketNumber = hash(key);
            Node<K, V> nodeInBucket = table[bucketNumber];
            while (nodeInBucket != null) {
                if (keysAreEqual(nodeInBucket.key, key)) {
                    return nodeInBucket.value;
                }
                nodeInBucket = nodeInBucket.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private boolean updateIfKeyPresent(K key, V value) {
        int hash = hash(key);
        Node<K, V> nodeInBucket = table[hash];
        while (nodeInBucket != null) {
            if (keysAreEqual(key, nodeInBucket.key)) {
                nodeInBucket.value = value;
                return true;
            }
            nodeInBucket = nodeInBucket.next;
        }
        return false;
    }

    private void createNewNode(K key, V value) {
        int hash = hash(key);
        Node<K, V> nodeWithHash = table[hash];
        if (nodeWithHash == null) {
            table[hash] = new Node<>(hash, key, value, null);
        } else {
            while (nodeWithHash.next != null) {
                nodeWithHash = nodeWithHash.next;
            }
            nodeWithHash.next = new Node<>(hash, key, value, null);
        }
        size++;
    }

    private boolean keysAreEqual(K keyFromMap, K key) {
        return keyFromMap == key || keyFromMap != null && keyFromMap.equals(key);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        initialize(capacity * DEFAULT_RESIZE_VALUE);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void initialize(int desiredCapacity) {
        capacity = desiredCapacity;
        table = (Node<K, V>[]) new Node[capacity];
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
    }
}
