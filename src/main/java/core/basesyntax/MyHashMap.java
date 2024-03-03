package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.capacity = table.length;
    }

    @Override
    public void put(K key, V value) {
        int threshold = (int) (capacity * LOAD_FACTOR);
        if (size > threshold) {
            resize();
        }

        int bucketIndex = getIndex(key);
        if (table[bucketIndex] != null) {
            Node<K, V> currentNode = table[bucketIndex];

            while (currentNode != null) {
                if (keyEquals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
        }

        Node<K, V> newNode = new Node<>(key, value);
        addNode(table, newNode);
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getIndex(key);
        Node<K, V> currentNode = table[bucketIndex];

        while (currentNode != null) {
            if (keyEquals(key, currentNode.key)) {
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

    private void resize() {
        Node<K, V>[] newTable = new Node[capacity << 1];
        capacity = newTable.length;

        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                currentNode.next = null;
                addNode(newTable, currentNode);
                currentNode = nextNode;
            }
        }
        table = newTable;
    }

    private void addNode(Node<K, V>[] table, Node<K, V> node) {
        int nodeIndex = getIndex(node.key);

        if (table[nodeIndex] == null) {
            table[nodeIndex] = node;
        } else {
            Node<K, V> tempNode = table[nodeIndex];
            while (tempNode.next != null) {
                tempNode = tempNode.next;
            }
            tempNode.next = node;
        }
    }

    private int getIndex(K key) {
        int hash = (key == null) ? 0 : Math.abs(key.hashCode());
        return hash % capacity;
    }

    private boolean keyEquals(K firstKey, K secondKey) {
        return firstKey == null ? secondKey == null : firstKey.equals(secondKey);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
