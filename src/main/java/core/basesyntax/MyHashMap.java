package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int indexBucket = calculateIndexBucket(key);
        Node<K, V> currentNode = table[indexBucket];
        if (size > threshold) {
            resize();
        }
        while (currentNode != null) {
            if ((key == currentNode.key) || (key != null
                    && key.equals(currentNode.key))) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[indexBucket];
        table[indexBucket] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = findNode(key);
        return (current == null) ? null : current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity * GROW_FACTOR;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] newTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : newTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int calculateIndexBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private Node<K, V> findNode(K key) {
        int indexBucket = calculateIndexBucket(key);
        Node<K, V> currentNode = table[indexBucket];
        while (currentNode != null) {
            if ((key == currentNode.key) || (key != null
                    && key.equals(currentNode.key))) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
