package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        initialize(DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        boolean keyPresent = updateIfKeyPresent(key, value);
        if (!keyPresent) {
            if (size >= capacity * DEFAULT_LOAD_FACTOR) {
                resize();
            }
            createNewNode(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNodeKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return key == null ? 0 : (key.hashCode() >= 0
                ? key.hashCode() : -key.hashCode()) % capacity;
    }

    private boolean updateIfKeyPresent(K key, V value) {
        Node<K, V> node = getNodeKey(key);
        if (node != null) {
            node.value = value;
            return true;
        }
        return false;
    }

    private void createNewNode(K key, V value) {
        int hash = hash(key);
        Node<K, V> nodeWithHash = table[hash];
        if (nodeWithHash == null) {
            table[hash] = new Node<>(key, value, null);
        } else {
            while (nodeWithHash.next != null) {
                nodeWithHash = nodeWithHash.next;
            }
            nodeWithHash.next = new Node<>(key, value, null);
        }
        size++;
    }

    private Node<K, V> getNodeKey(K key) {
        int hash = hash(key);
        Node<K, V> nodeInBucket = table[hash];
        while (nodeInBucket != null) {
            if (keysAreEqual(nodeInBucket.key, key)) {
                return nodeInBucket;
            }
            nodeInBucket = nodeInBucket.next;
        }
        return null;
    }

    private boolean keysAreEqual(K keyFromMap, K key) {
        return keyFromMap == key || (keyFromMap != null && keyFromMap.equals(key));
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        initialize(capacity * 2);
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
        size = 0;
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
