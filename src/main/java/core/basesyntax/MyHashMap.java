package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_ENLARGEMENT = 2;
    private static final int EMPTY_MAP_SIZE = 0;

    private Node[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        this.capacity = INITIAL_CAPACITY;
        this.size = EMPTY_MAP_SIZE;
    }

    public static class Node<K, V> {
        private final K key;
        private V value;
        private final Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.key != null && node.key.equals(key)) || (node.key == null && key == null)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size > capacity * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.key != null && node.key.equals(key)) || (node.key == null && key == null)) {
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

    private int hash(K key) {
        int hash = (key == null) ? 0 : key.hashCode() % capacity;
        if (hash < 0) {
            hash *= -1;
        }
        return hash;
    }

    private void resize() {
        Node<K, V>[] oldTable;
        oldTable = table;
        capacity *= CAPACITY_ENLARGEMENT;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                put(node.key, node.value);
                Node<K, V> nextNode = node.next;
                while (nextNode != null) {
                    put(nextNode.key, nextNode.value);
                    nextNode = nextNode.next;
                }
            }
        }
        oldTable = null;
    }
}

