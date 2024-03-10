package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private Node<K, V> [] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            initTable();
        }
        if (size == threshold) {
            resize();
        }
        Node<K, V> existingNodeByKey = getNodeWithValue(key);
        if (existingNodeByKey != null) {
            existingNodeByKey.value = value;
            return;
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        addNodeToTable(newNode);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table == null || table[getBucket(key)] == null) {
            return null;
        }
        Node<K, V> node = table[getBucket(key)];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
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

    private void initTable() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        table = new Node[capacity];
        threshold = (int)(capacity * DEFAULT_LOAD_FACTOR);
    }

    private int getBucket(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        Node<K, V> [] oldTable = table;
        size = 0;
        int oldcapacity = capacity;
        capacity *= 2;
        table = new Node[capacity];
        for (int i = 0; i < oldcapacity; i++) {
            if (oldTable[i] != null) {
                Node<K, V> node = oldTable[i];
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
        threshold = (int)(capacity * DEFAULT_LOAD_FACTOR);
    }

    private Node<K, V> getNodeWithValue(K key) {
        Node<K, V> node = table[getBucket(key)];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void addNodeToTable(Node<K, V> node) {
        if (size == 0 || table[getBucket(node.key)] == null) {
            table[getBucket(node.key)] = node;
            return;
        }
        Node<K, V> lastNodeInBucket = table[getBucket(node.key)];
        while (lastNodeInBucket.next != null) {
            lastNodeInBucket = lastNodeInBucket.next;
        }
        lastNodeInBucket.next = node;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
