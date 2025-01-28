package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_ENLARGEMENT = 2;
    private static final int EMPTY_MAP_SIZE = 0;

    private Node<K, V>[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        this.capacity = table.length;
        this.size = EMPTY_MAP_SIZE;
    }

    public static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

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
        Node<K, V> newNode = new Node<K, V>(key, value, table[index]);
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
        int newCapacity = capacity * CAPACITY_ENLARGEMENT;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < capacity; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int index = hash(node.key);
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
    }
}
