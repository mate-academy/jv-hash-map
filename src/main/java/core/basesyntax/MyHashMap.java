package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K,V>[] table;
    private int capacity;
    private int threshold;
    private double loadFactor;

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = Math.abs(hash % capacity);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> previousNode = null;
            while (currentNode != null) {
                if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.next = new Node<>(hash, key, value, null);
            size++;
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key) % capacity);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
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

    public boolean isEmpty() {
        return size == 0;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        capacity = capacity * 2;
        threshold = (int) (capacity * loadFactor);
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                Node<K, V> next = oldNode.next;
                int index = Math.abs(oldNode.hash % capacity);
                oldNode.next = table[index];
                table[index] = oldNode;
                oldNode = next;
            }
        }
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
