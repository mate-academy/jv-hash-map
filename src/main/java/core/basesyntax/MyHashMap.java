package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getBucketIndex(key);
        Node<K, V> existingNode = findNode(key, index);
        if (existingNode != null) {
            existingNode.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value, table[index]);
            table[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> node = findNode(key, index);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

     private void resize() {
        int newCapacity = table.length * CAPACITY_MULTIPLIER;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        final Node<K, V>[] oldTable = table; 
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private Node<K, V> findNode(K key, int index) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}