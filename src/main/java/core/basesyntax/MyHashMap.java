package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int PRIME1 = 1;
    private static final int PRIME2 = 17;
    private int capacity;
    private int size = 0;
    private Node<K,V>[] table;

    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = hash % capacity;
        Node<K,V> current = table[index];
        Node<K,V> prev = null;

        while (current != null) {
            if (key == null && current.key == null) {
                current.value = value;
                return;
            } else if (key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }

        Node<K,V> newNode = new Node<>(hash, key, value);
        if (prev == null) {
            table[index] = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
        if (size >= (capacity * LOAD_FACTOR)) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % capacity;
        int hash = hash(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (key == null && node.key == null) {
                return node.value;
            } else if (node.hash == hash && node.key.equals(key)) {
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

    public int hash(K key) {
        int result = 0;
        if (key == null) {
            return result;
        }
        result = (PRIME1 * PRIME2) + key.hashCode();
        if (result < 0) {
            result *= (-1);
        }
        return result;
    }

    public void resize() {
        capacity *= 2;
        Node<K,V>[] newTable = new Node[capacity];
        for (Node<K,V> node : table) {
            while (node != null) {
                int index = node.hash % capacity;
                Node<K,V> temp = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = temp;
            }
        }
        table = newTable;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
