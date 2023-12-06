package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    public static final int GROWTH_FACTOR = 2;

    private int size;
    private Node<K, V>[] storage;

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            storage = new Node[INITIAL_CAPACITY];
        }

        int hash = getKeyHash(key);
        int storageIndex = getStorageIndex(hash, storage.length);

        Node<K, V> currentNode = storage[storageIndex];
        Node<K, V> prevNode = null;

        while (currentNode != null) {
            if (areKeysEqual(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }

        Node<K, V> newNode = new Node<>(key, value, hash);
        if (prevNode != null) {
            prevNode.next = newNode;
        } else {
            storage[storageIndex] = newNode;
        }
        ++size;
        resizeIfNeeded();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getKeyHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static int getStorageIndex(int hash, int storageLength) {
        return Math.abs(hash) % storageLength;
    }

    private void resizeIfNeeded() {
        if (size < storage.length * LOAD_FACTOR) {
            return;
        }

        int newCapacity = storage.length * GROWTH_FACTOR;
        Node<K, V>[] newStorage = new Node[newCapacity];
        Node<K, V>[] lastNodes = new Node[newCapacity];

        for (int i = 0; i < storage.length; i++) {
            Node<K, V> currentNode = storage[i];
            while (currentNode != null) {
                int newStorageIndex = getStorageIndex(currentNode.hash, newCapacity);
                Node<K, V> lastNodeInRow = lastNodes[newStorageIndex];
                if (lastNodeInRow == null) {
                    lastNodes[newStorageIndex] = currentNode;
                    newStorage[newStorageIndex] = currentNode;
                } else {
                    lastNodeInRow.next = currentNode;
                    lastNodes[newStorageIndex] = currentNode;
                }
                currentNode = currentNode.next;
            }
        }
        for (int i = 0; i < newCapacity; i++) {
            if (lastNodes[i] != null) {
                lastNodes[i].next = null;
            }
        }
        storage = newStorage;
    }

    private Node<K, V> findNode(K key) {
        if (storage == null) {
            return null;
        }
        int hash = getKeyHash(key);
        int storageIndex = getStorageIndex(hash, storage.length);

        Node<K, V> currentNode = storage[storageIndex];
        while (currentNode != null && !areKeysEqual(currentNode.key, key)) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private boolean areKeysEqual(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    private static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, int hash) {
            this.key = key;
            this.hash = hash;
            this.value = value;
        }
    }
}
