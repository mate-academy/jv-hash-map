package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;

    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this(INITIAL_CAPACITY);
    }

    public MyHashMap(int capacity) {
        this.table = new Node[capacity];
        this.threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K,V> newNode = new Node<>(key, value);

        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (key == current.key || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (key == current.key || key != null && key.equals(current.key)) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % table.length;
        }
    }

    private void resize() {
        int newCapacity = table.length * RESIZE_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = node.key == null ? 0 :
                        Math.abs(node.key.hashCode()) % newCapacity;
                Node<K, V> next = node.next;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;

        private Node<K,V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
