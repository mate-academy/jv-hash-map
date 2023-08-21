package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int TABLE_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (((float) size / capacity) > LOAD_FACTOR) {
            resize();
        }
        int hashCode = key == null ? 0 : key.hashCode();
        int position = Math.abs(hashCode) % capacity;
        Node<K, V> current = table[position];
        while (current != null) {
            if (key == null && current.key == null || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, hashCode, table[position]);
        table[position] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        int position = Math.abs(hashCode) % capacity;
        Node<K, V> node = table[position];
        while (node != null) {
            if (node.key == null && key == null || node.key != null && node.key.equals(key)) {
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
        int newCapacity = capacity * TABLE_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node: this.table) {
            Node<K, V> current = node;
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = Math.abs(current.hash) % newCapacity;
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        this.table = newTable;
        capacity = newCapacity;
    }

    private static class Node<K, V> {
        private V value;
        private Node<K, V> next;
        private final K key;
        private final int hash;

        public Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
