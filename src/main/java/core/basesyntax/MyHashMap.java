package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndexBucket(key);
        Node<K,V> node = table[index];
        Node<K,V> newNode = new Node<>(key, value);
        if (node == null) {
            table[index] = newNode;
        } else {
            while (node != null) {
                if (key == node.key || key != null && key.equals((node.key))) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    newNode.next = table[index];
                    table[index] = newNode;
                }
                node = node.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexBucket(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
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

    public int getIndexBucket(K key) {
        return key == null ? 0 : hash(key) % DEFAULT_CAPACITY;
    }

    public int hash(K key) {
        return Math.abs(key.hashCode());
    }

    private void resize() {
        capacity = capacity * 2;
        Node<K, V>[] newTable = new Node[capacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndexBucket(node.key);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
