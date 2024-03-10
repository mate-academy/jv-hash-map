package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY= 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private Node<K, V> [] table;
    private int size;
    private int threshold;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        resize();
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

    private int getBucket(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        if (table == null) {
            table = new Node[capacity];
            threshold = (int)(capacity * DEFAULT_LOAD_FACTOR);
        }
        if (size == threshold) {
            int oldcapasity = capacity;
            capacity *= 2;
            Node<K, V> [] oldTable = table;
            table = new Node[capacity];
            size = 0;
            for (int i = 0; i < oldcapasity; i++) {
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
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
