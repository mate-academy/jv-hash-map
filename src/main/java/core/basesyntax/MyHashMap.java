package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int INCREASE_CAPACITY = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        threshold = (int) (INCREASE_CAPACITY * DEFAULT_LOAD_FACTOR);
        nodes = new Node[capacity];
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int hashPosition = getNodeIndex(key, capacity);
        Node<K, V> positionNode = nodes[hashPosition];
        if (positionNode == null) {
            nodes[hashPosition] = newNode;
            size++;
            return;
        }
        while (positionNode != null) {
            if ((key == positionNode.key) || (key != null && key.equals(positionNode.key))) {
                positionNode.value = value;
                return;
            }
            if (positionNode.next == null) {
                positionNode.next = newNode;
                size++;
                return;
            }
            positionNode = positionNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = nodes[getNodeIndex(key, capacity)];
        while (getNode != null) {
            if ((key == getNode.key) || (key != null && key.equals(getNode.key))) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getNodeIndex(K key, int capacity) {
        int hash = hash(key) % capacity;
        return hash < 0 ? Math.abs(hash) : hash;
    }

    private void checkSize() {
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        size = 0;
        capacity *= INCREASE_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldArray = nodes;
        nodes = new Node[capacity];
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}