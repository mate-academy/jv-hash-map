package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold;
    private Node<K,V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        int index;
        Node<K,V> tail = null;
        Node<K,V> newNode = new Node<>(hash(key), key, value, null);
        if (size == threshold) {
            resize();
        }
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % capacity);
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
        int index;
        if (size == 0) {
            return null;
        }
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % capacity);
        }
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

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private Node<K,V> [] resize() {
        Node<K,V>[] oldTab = table;
        Node<K,V> currentNode;
        if (table == null) {
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            return table = new Node[DEFAULT_INITIAL_CAPACITY];
        } else {
            size = 0;
            capacity *= CAPACITY_MULTIPLIER;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            table = new Node[capacity];
            for (int i = 0; i < oldTab.length; i++) {
                currentNode = oldTab[i];
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
        return table;
    }
}
