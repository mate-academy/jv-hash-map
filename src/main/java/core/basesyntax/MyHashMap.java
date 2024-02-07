package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int size;
    private Node<K, V>[] table;
    private final float loadFactor;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
    }

    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hash = hash(key.hashCode());
        int index = indexFor(hash, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.key == key || key.equals(node.key))) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int hash = hash(key.hashCode());
        int index = indexFor(hash, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.key == key || key.equals(node.key))) {
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

    private void putForNullKey(V value) {
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            if (node == null) {
                table[i] = new Node<>(0, null, value, null);
                size++;
                return;
            } else if (node.key == null) {
                node.value = value;
                return;
            }
        }
        resize();
        putForNullKey(value);
    }

    private V getForNullKey() {
        for (Node<K, V> node : table) {
            if (node != null && node.key == null) {
                return node.value;
            }
        }
        return null;
    }

    private void addNode(int hash, K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;
        if (size >= table.length * loadFactor) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                Node<K, V> next = oldNode.next;
                int newIndex = indexFor(oldNode.hash, newCapacity);
                oldNode.next = newTable[newIndex];
                newTable[newIndex] = oldNode;
                oldNode = next;
            }
        }
        table = newTable;
    }

    private int hash(int hashCode) {
        return hashCode ^ (hashCode >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private static class Node<K, V> {
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
