package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = index(hash, table.length);
        Node<K, V> node = getNode(key, hash, index);
        if (node != null) {
            node.value = value;
        } else {
            addNode(hash, key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = index(hash, table.length);
        Node<K, V> node = getNode(key, hash, index);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int index(int hash, int length) {
        return Math.abs(hash % (length - 1));
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        if (size++ >= threshold) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = index(node.hash, newCapacity);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private Node<K, V> getNode(K key, int hash, int index) {
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash
                    && (node.key == key || node.key != null && node.key.equals(key))) {
                return node;
            }
        }
        return null;
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
