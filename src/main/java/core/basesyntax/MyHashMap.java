package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

    private int capacity = DEFAULT_CAPACITY;
    private float loadFactor = DEFAULT_LOAD_FACTOR;
    private int threshold;
    private int size;
    private Node<K, V> [] table;

    {
        table = new Node[capacity];
        threshold = computeThreshold();
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        int index = computeIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K,V> node = table[index];
            while (true) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    node.value = value;
                    break;
                } else if (node.next != null) {
                    node = node.next;
                } else {
                    node.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = computeIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int computeThreshold() {
        return (int) (capacity * loadFactor);
    }

    private void checkCapacity() {
        if (size >= threshold) {
            setDoubleCapacity();
        }
    }

    private void setDoubleCapacity() {
        Node<K, V>[] nodes = new Node[size];
        int index = 0;
        for (Node<K, V> node : table) {
            while (node != null) {
                nodes[index++] = node;
                node = node.next;
            }
        }
        capacity = capacity * 2;
        table = new Node[capacity];
        size = 0;
        threshold = computeThreshold();
        for (Node<K, V> node : nodes) {
            put(node.key, node.value);
        }
    }

    private int computeIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private static class Node<T, V> {
        private final T key;
        private V value;
        private Node<T,V> next;

        public Node(T key, V value, Node<T, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
