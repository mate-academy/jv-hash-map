package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] storage;
    private int capacity;
    private int size;
    private int threshold;

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (capacity == 0) {
            storage = new Node[DEFAULT_CAPACITY];
            capacity = DEFAULT_CAPACITY;
            threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        resizeIfNeed();
        if (add(key, value)) {
            ++size;
        }
    }

    @Override
    public V getValue(K key) {
        if (storage == null) {
            return null;
        }
        Node<K, V> node = storage[getIndex(key)];
        if (node != null) {
            K nodeKey;
            do {
                nodeKey = node.key;
                if ((nodeKey == key || nodeKey != null && nodeKey.equals(key))) {
                    return node.value;
                }
                node = node.next;
            } while (node != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeed() {
        if (size == threshold) {
            capacity *= 2;
            Node<K, V>[] oldStorage = storage;
            storage = new Node[capacity];
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            for (Node<K, V> node : oldStorage) {
                if (node != null) {
                    do {
                        add(node.key, node.value);
                        node = node.next;
                    } while (node != null);
                }
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        return Math.abs(getHash(key)) % capacity;
    }

    private boolean add(K key, V value) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            int index = getIndex(key);
            storage[index] = new Node<>(getHash(key), key, value, storage[index]);
            return true;
        } else {
            node.value = value;
        }
        return false;
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> node = storage[getIndex(key)];
        if (node != null) {
            K nodeKey;
            do {
                nodeKey = node.key;
                if ((nodeKey == key || nodeKey != null && nodeKey.equals(key))) {
                    return node;
                }
                node = node.next;
            } while (node != null);
        }
        return null;
    }
}
