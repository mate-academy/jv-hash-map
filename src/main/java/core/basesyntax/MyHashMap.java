package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
            put(key, value);
        } else {
            int hash = hash(key);
            Node<K, V> newNode = new Node<>(hash, key, value, null);
            if (table[hash] == null) {
                table[hash] = newNode;
            } else {
                Node<K, V> currentNode = table[hash];
                currentNode = getNode(key, value, currentNode);
                if (currentNode == null) {
                    return;
                }
                currentNode.next = newNode;
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        if (node == null) {
            return null;
        }
        do {
            if (isKeysEqual(key, node)) {
                return node.value;
            }
        }
        while ((node = node.next) != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key, V value, Node<K, V> currentNode) {
        while (true) {
            if (isKeysEqual(key, currentNode)) {
                currentNode.value = value;
                return null;
            }
            if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                break;
            }
        }
        return currentNode;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private boolean isKeysEqual(K key, Node<K, V> currentNode) {
        return key == null && currentNode.key == null
                || currentNode.key != null && currentNode.key.equals(key);
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] resizeTable = new Node[capacity];
        repositionExistNodes(resizeTable);
    }

    private void repositionExistNodes(Node<K, V>[] resizeTable) {
        Node<K, V>[] copyTable = table;
        table = resizeTable;
        size = 0;
        for (Node<K, V> node : copyTable) {
            if (node != null) {
                do {
                    put(node.key, node.value);
                } while ((node = node.next) != null);
            }
        }
    }

    private class Node<K, V> {
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
}