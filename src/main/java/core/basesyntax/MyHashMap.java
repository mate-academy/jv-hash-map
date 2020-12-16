package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            table = resize();
        }
        setNode(new Node<>(key, value, null));
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode = table[getHash(key) % capacity];
        while (currentNode != null) {
            if (currentNode.key == key
                    || (currentNode.key != null && currentNode.key.equals(key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void setNode(Node<K, V> newNode) {
        int indexOfBucket = getHash(newNode.key) % capacity;
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = newNode;
        } else {
            Node<K, V> currentNode = table[indexOfBucket];
            while (currentNode != null) {
                if (newNode.key == currentNode.key
                        || (newNode.key != null && newNode.key.equals(currentNode.key))) {
                    currentNode.value = newNode.value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
    }

    private Node<K, V>[] resize() {
        capacity = capacity * 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] oldMap = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (Node<K, V> currentNode : oldMap) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return table;
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() * 19);
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
