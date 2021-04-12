package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASE_SIZE = 2;
    private int capacity;
    private int size;
    private int threshold;

    private Node<K, V>[] container;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        this.container = (Node<K, V>[]) new Node[capacity];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = key == null ? 0 : getIndexUsingHashcode(key);

        Node<K, V> currentNode = container[index];
        while (currentNode != null) {
            if (currentNode.key == key || currentNode.key != null && currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        if (size++ > threshold) {
            increaseSize();
        }

        Node<K, V> newNode = new Node<>(key, value, null);
        if (container[index] == null) {
            container[index] = newNode;
        } else {
            newNode.next = container[index].next;
            container[index].next = newNode;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> lookingNode = container[key == null ? 0 : getIndexUsingHashcode(key)];
        while (lookingNode != null) {
            if (lookingNode.key == key || lookingNode.key != null && lookingNode.key.equals(key)) {
                return lookingNode.value;
            }
            lookingNode = lookingNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void increaseSize() {
        capacity *= INCREASE_SIZE;
        threshold *= INCREASE_SIZE;
        size = 0;
        Node<K, V>[] oldContainer = container;
        container = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : oldContainer) {
            Node<K, V> temp = node;
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
            }
        }
        size++;
    }

    private int getIndexUsingHashcode(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
}
