package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        capacity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (isEnoughSpace(size) == false) {
            resize(capacity);
        }
        if (key == null) {
            putForNullKey(value);
        } else {
            int h = hash(key);
            int index = indexFor(h, capacity);
            Node<K, V> nodes = table[index];
            while (nodes != null) {
                if (nodes.key != null && nodes.key.equals(key)) {
                    nodes.value = value;
                    return;
                }
                nodes = nodes.next;
            }
            addNode(key, value, index);
            size++;
        }
    }

    private void putForNullKey(V value) {
        Node<K, V> node = table[0];
        while (node != null) {
            if (node.key == null) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(null, value, 0);
        size++;
    }

    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private int indexFor(int h, int length) {
        return h & (length - 1);
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> node = table[index];
        table[index] = new Node<>(key, value, node);
    }

    private void resize(int newCapacity) {
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[newCapacity];
        table = newTable;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            V v = getForNullKey();
            return v;
        }
        int h = hash(key);
        int index = indexFor(h, capacity);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key != null && node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private V getForNullKey() {
        Node<K, V> node = table[0];
        while (node != null) {
            if (node.key == null) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private boolean isEnoughSpace(int length) {
        if (length <= threshold) {
            return true;
        }
        capacity = capacity * 2;
        size = 0;
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
