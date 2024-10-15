package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;
    private int threshold;
    private Node<K, V> [] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = (hash(key) & 0x7FFFFFFF) % table.length;
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = new Node<>(hash(key), key, value, null);
            size++;
            checkResize();
            return;
        }
        while (true) {
            if (current.key != null && current.key.equals(key)) {
                current.value = value;
                return;
            } else if (current.key == null && key == null) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                break;
            }
            current = current.next;
        }
        current.next = new Node<>(hash(key), key, value, null);
        size++;
        checkResize();
    }

    @Override
    public V getValue(K key) {
        int index = (hash(key) & 0x7FFFFFFF) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key != null && current.key.equals(key)) {
                return current.value;
            } else if (current.key == null && key == null) {
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

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = (hash(node.key) & 0x7FFFFFFF) % newCapacity;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private void checkResize() {
        if (size >= threshold) {
            resize();
        }
    }

    static class Node<K, V> {
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
}
