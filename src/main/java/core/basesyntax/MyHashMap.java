package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_GROW_FACTOR = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K,V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = defineIndexByKey(key);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if ((key == currentNode.key) || (key != null && key.equals(currentNode.key))) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = searchNodeByKey(key);
        return (node == null) ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity * DEFAULT_GROW_FACTOR;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node[] temporalTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : temporalTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int defineIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private Node<K, V> searchNodeByKey(K key) {
        int index = defineIndexByKey(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if ((key == currentNode.key) || (key != null && key.equals(
                    currentNode.key))) {
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

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
