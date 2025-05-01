
package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int RESIZE_MULTIPLIER = 2;
    private int threshold;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.capacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putNode(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = calculateIndex(key);
        if (table[hash] == null) {
            return null;
        }
        Node<K, V> currentNode = table[hash];
        while (currentNode != null) {
            if (key == null && currentNode.key == null) {
                return currentNode.value;
            } else if (key != null && key.equals(currentNode.key)) {
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

    private void putNode(K key, V value) {
        int hash = calculateIndex(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value);
            size++;
            return;
        }
        putLinkedNode(key, value);
    }

    private void putLinkedNode(K key, V value) {
        Node<K, V> currentNode = table[calculateIndex(key)];
        while (currentNode != null) {
            if (key == null && currentNode.key == null
                    || key != null && key.equals(currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private int calculateIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        capacity *= RESIZE_MULTIPLIER;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putNode(node.key, node.value);
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
