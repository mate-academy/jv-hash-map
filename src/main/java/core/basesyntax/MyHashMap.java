package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MULTIPLIER = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }
    
    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndexByHash(key);
        if (setValueIfKeyExist(index, key, value)) {
            return;
        }
        Node<K, V> newNode;
        if (key == null) {
            newNode = new Node<>(null, value, null);
        } else {
            newNode = new Node<>(key, value, null);
        }
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K,V> currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByHash(key);
        if (table[index] == null) {
            return null;
        }
        Node<K,V> currentNode = table[index];
        while (currentNode.next != null) {
            if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexByHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private boolean setValueIfKeyExist(int index, K key, V value) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
                currentNode.value = value;
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    private void resize() {
        int capacity = table.length;
        double threshold = capacity * LOAD_FACTOR;
        if (size == threshold) {
            transfer(capacity);
        }
    }

    private void transfer(int capacity) {
        Node<K, V>[] oldArray = table;
        table = (Node<K, V>[]) new Node[capacity * MULTIPLIER];
        size = 0;

        for (Node<K, V> kvNode : oldArray) {
            Node<K, V> currentNode = kvNode;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
