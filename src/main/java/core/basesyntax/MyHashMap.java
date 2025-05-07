package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = new Node<>(key, value, null);
            size++;
        } else {
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

    private void resizeIfNeeded() {
        if (size >= capacity * DEFAULT_LOAD_FACTOR) {
            final Node<K, V>[] oldTable = table;
            capacity *= 2;
            table = (Node<K, V>[]) new Node[capacity];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private void createNewNode(K key, V value) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<K, V>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    private Node<K, V> getNodeKey(K key) {
        int hash = getIndex(key);
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

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
}
