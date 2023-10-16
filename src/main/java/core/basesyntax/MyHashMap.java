package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private final float loadFactor;
    private int size;
    private int capacity;
    private Node<K,V>[] table;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size > capacity * loadFactor) {
            resize();
        }
        int index = getIndex(key);
        Node node = table[index];
        while (node != null) {
            if ((node.key != null && node.key.equals(key))
                    || node.key == null && key == null) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K,V> newNode = new Node<>(key,value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if ((node.key != null && node.key.equals(key))
                    || node.key == null && key == null) {
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

    private void resize() {
        capacity = capacity * CAPACITY_MULTIPLIER;
        Node[] newTable = new Node[capacity];
        for (int i = 0; i < capacity / CAPACITY_MULTIPLIER; i++) {
            Node<K,V> node = table[i];
            while (node != null) {
                int index = getIndex(node.key);
                var currentNode = newTable[index];
                if (currentNode == null) {
                    newTable[index] = new Node<>(node.key, node.value);
                } else {
                    while (currentNode.next != null) {
                        currentNode = currentNode.next;
                    }
                    currentNode.next = new Node<>(node.key, node.value);
                }
                node = node.next;
            }
        }
        table = newTable;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode() % capacity;
        return hash >= 0 ? hash : -1 * hash;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
