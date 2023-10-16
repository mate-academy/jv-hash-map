package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K,V> tail = null;
        Node<K,V> newNode = new Node<>(key, value);
        if (size == threshold) {
            resize();
        }
        Node<K,V> currentNode = table[index];
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            while (currentNode != null) {
                if (currentNode.key == key
                        || currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    tail = currentNode;
                }
                currentNode = currentNode.next;
            }
            tail.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key || currentNode.key != null && currentNode.key.equals(key)) {
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

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K,V> [] resize() {
        final Node<K,V>[] oldTable = table;
        Node<K,V> currentNode;
        size = 0;
        capacity *= CAPACITY_MULTIPLIER;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return table;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }
}
