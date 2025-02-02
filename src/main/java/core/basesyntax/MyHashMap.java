package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;
    private int capacity;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    private void putVal(int hash, K key, V value) {
        if (size >= capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = (key == null) ? 0 : hash % capacity;
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> current = table[index];

            while (true) {
                if ((current.key == key) || (key != null && key.equals(current.key))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(hash, key, value, null);
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = (node.key == null) ? 0 : Math.abs(node.key.hashCode() % newCapacity);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (key == null && current.key == null || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
