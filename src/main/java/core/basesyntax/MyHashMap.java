package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);

        if (size > threshold) {
            resize();
        }

        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (checkKey(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (checkKey(currentNode.key, key)) {
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

    private int calculateIndex(K key) {
        return Math.abs(key == null ? 0 : key.hashCode()) % table.length;
    }

    private boolean checkKey(K nodeKey, K key) {
        return nodeKey == key || (nodeKey != null && nodeKey.equals(key));
    }

    private void resize() {
        threshold *= 2;
        Node<K, V>[] tempNode = table;
        table = new Node[table.length * 2];
        size = 0;
        Node<K, V> currentNode;
        for (Node<K, V> node: tempNode) {
            currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
