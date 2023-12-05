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

        int hash = keyHash(key);
        int storageIndex = storageIndex(hash, storage.length);

        Node<K, V> currentNode = storage[storageIndex];
        Node<K, V> prevNode = null;

        while (true) {
            if (currentNode == null) {
                Node<K, V> newNode = new Node<>(key, value);
                if (prevNode != null) {
                    prevNode.next = newNode;
                } else {
                    storage[storageIndex] = newNode;
                }
                ++size;
                resizeIfNeeded();
                break;
            }
            if (areObjectsEqual(currentNode.key, key)) {
                currentNode.value = value;
                break;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
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

    private static int keyHash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static int storageIndex(int hash, int storageLenght) {
        return Math.abs(hash % storageLenght);
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
                int newStorageIndex = storageIndex(currentNode.hash, newCapacity);
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
        int hash = keyHash(key);
        int storageIndex = storageIndex(hash, storage.length);

        Node<K, V> currentNode = storage[storageIndex];
        while (true) {
            if (currentNode == null || areObjectsEqual(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
    }

    private class Node<T, S> {
        private final T key;
        private final int hash;
        private S value;
        private Node<T, S> next;

        public Node(T key, S value) {
            this.key = key;
            this.hash = keyHash(key);
            this.value = value;
        }

    }

    private static boolean areObjectsEqual(Object obj1, Object obj2) {
        return obj1 == obj2 || obj1 != null && obj1.equals(obj2);
    }
}
