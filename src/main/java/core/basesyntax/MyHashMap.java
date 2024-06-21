package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || (key != null && key.equals(node.key)))) {
                node.value = value;
                return;
            }
        }

        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || (key != null && key.equals(node.key)))) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public static class Node<K, V> {
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

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        if (++size > threshold) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = indexFor(node.hash, newCapacity);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }
}
