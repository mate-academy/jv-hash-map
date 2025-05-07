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
        int index = getIndex(key);
        Node<K, V> oldNode = table[index];
        while (oldNode != null) {
            if (key == null && oldNode.key == null || key != null && key.equals(oldNode.key)) {
                oldNode.value = value;
                return;
            }
            oldNode = oldNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> findNode = table[getIndex(key)];
        while (findNode != null) {
            if (key == findNode.key || key != null && key.equals(findNode.key)) {
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
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        int newCapacity = capacity * DEFAULT_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node: table) {
            while (node != null) {
                int index = getIndex(node.key);
                Node<K, V> nextNode = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = nextNode;
            }
        }
        table = newTable;
    }
}
