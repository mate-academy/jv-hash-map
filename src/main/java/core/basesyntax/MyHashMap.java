package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;
    private final int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size / LOAD_FACTOR > capacity) {
            resize();
        }
        Node<K, V> oldNode = table[getIndexHash(key)];
        while (oldNode != null) {
            if (key == null && oldNode.key == null || key != null && key.equals(oldNode.key)) {
                size--;
            }
            oldNode = oldNode.next;
        }
        Node<K, V> newNode = new Node<>(getIndexHash(key), key, value, table[getIndexHash(key)]);
        table[getIndexHash(key)] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> findNode = table[getIndexHash(key)];
        while (findNode != null) {
            if (key == null && findNode.key == null || key != null && key.equals(findNode.key)) {
                return findNode.value;
            }
            findNode = findNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndexHash(K key) {
        if (key == null) {
            return 0;
        }
        int keyHash = key.hashCode();
        if (keyHash < 0) {
            keyHash = - keyHash;
        }
        return keyHash % capacity;
    }

    private void resize() {
        int newCapacity = capacity * DEFAULT_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node: table) {
            while (node != null) {
                Node<K, V> newNode = node.next;
                node.next = newTable[getIndexHash(node.key)];
                newTable[getIndexHash(node.key)] = node;
                node = newNode;
            }
        }
        table = newTable;
    }
}

