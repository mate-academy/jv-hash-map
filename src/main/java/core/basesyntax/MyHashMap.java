package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node(key, value, null);
            size++;
        } else {
            Node<K, V> previousNode = null;
            boolean replaced = false;
            while (node != null) {
                if (node.key == key || (key != null && key.equals(node.key))) {
                    node.value = value;
                    replaced = true;
                    break;
                }
                previousNode = node;
                node = node.next;
            }
            if (previousNode != null && !replaced) {
                previousNode.next = new Node(key, value, null);
                size++;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        capacity = newCapacity;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K,V>[] newTable = new Node[capacity];
        for (int i = 0; i < table.length; i++) {
            Node<K,V> currNode = table [i];
            if (currNode == null) {
                continue;
            }
            while (currNode != null) {
                K currKey = currNode.key;
                int newIndex = getIndex(currKey);
                if (newTable[newIndex] == null) {
                    newTable[newIndex] = currNode;
                } else {
                    Node<K,V> presentNode = newTable[newIndex];
                    while (presentNode.next != null) {
                        presentNode = presentNode.next;
                    }
                    presentNode.next = currNode;
                }
                Node<K, V> nextNode = currNode.next;
                currNode.next = null;
                currNode = nextNode;
            }
        }
        table = newTable;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> currNode = table [index];
        while (currNode != null) {
            if (currNode.key == key || (key != null && key.equals(currNode.key))) {
                V value = currNode.value;
                return value;
            }
            currNode = currNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}

