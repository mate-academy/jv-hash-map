package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_CAPACITY_GROWTH_MULTIPLIER = 2;

    private int capacity = DEFAULT_CAPACITY;
    private float loadFactor = DEFAULT_LOAD_FACTOR;
    private int threshold;
    private int capacityGrowthMultiplier = DEFAULT_CAPACITY_GROWTH_MULTIPLIER;
    private int size;
    private Node<K, V> [] table;

    {
        table = new Node[capacity];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
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
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[computeIndex(key)];
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

    private void checkCapacity() {
        if (size == threshold) {
            setCapacity();
        }
    }

    private void setCapacity() {
        Node<K, V>[] nodes = new Node[size];
        int index = 0;
        for (Node<K, V> node : table) {
            while (node != null) {
                nodes[index++] = node;
                node = node.next;
            }
        }
        capacity *= capacityGrowthMultiplier;
        table = new Node[capacity];
        size = 0;
        threshold = (int) (capacity * loadFactor);;
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
