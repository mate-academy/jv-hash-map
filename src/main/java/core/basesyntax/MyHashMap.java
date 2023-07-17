package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }
    
    @Override
    public void put(K key, V value) {
        resize();
        int index = hash(key);
        if (checkIfKeysAreEqual(index, key, value)) {
            return;
        }
        Node<K,V> newNode = createNewNode(key, value);
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
        int index = hash(key);
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

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node<K, V> createNewNode(K key, V value) {
        Node<K, V> newNode;
        if (key == null) {
            newNode = new Node<>(0, null, value, null);
        } else {
            newNode = new Node<>(key.hashCode(), key, value, null);
        }
        return newNode;
    }

    private boolean checkIfKeysAreEqual(int index, K key, V value) {
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
        table = (Node<K, V>[]) new Node[capacity * 2];
        size = 0;

        for (int i = 0; i < oldArray.length; i++) {
            Node<K,V> currentNode = oldArray[i];
            while (currentNode != null) {
                Node<K,V> nextNode = currentNode.next;
                int newIndex = hash(currentNode.key);
                currentNode.next = table[newIndex];
                table[newIndex] = currentNode;
                currentNode = nextNode;
                size++;
            }
        }
    }

    static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
