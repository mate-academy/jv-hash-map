package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.key != null && node.key.equals(key)) || node.key == key) {
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

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void putValue(int hash, K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int index = hash;
        Node<K, V> node = table[index];
        Node<K, V> newNode = new Node<>(hash, key, value, null);

        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            } else if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
        table[index] = newNode;
        size++;
    }

    private void resize() {
        capacity = capacity * RESIZE_COEFFICIENT;
        threshold = (int) (LOAD_FACTOR * capacity);

        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        transferNodesFromOldTable(oldTable);
    }

    private void transferNodesFromOldTable(Node[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private class Node<K, V> {
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
