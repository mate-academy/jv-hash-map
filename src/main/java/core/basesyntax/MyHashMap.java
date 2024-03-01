package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private int capacity;
    private int threshold;
    private Node<K, V>[] elementsStorage;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        elementsStorage = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfThresholdReached();
        int hashCode = getHash(key);
        int bucketIndex = getIndexByHash(hashCode);
        Node<K, V> node = new Node<>(hashCode, key, value, null);
        Node<K, V> currentNode = elementsStorage[bucketIndex];
        if (currentNode == null) {
            elementsStorage[bucketIndex] = node;
            size++;
            return;
        }
        while (true) {
            if (isKeysEquals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int hashCode = getHash(key);
        int bucketIndex = getIndexByHash(hashCode);
        Node<K, V> currentNode = elementsStorage[bucketIndex];
        while (currentNode != null) {
            if (isKeysEquals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndexByHash(int hashCode) {
        return Math.abs(hashCode) % capacity;
    }

    private boolean isKeysEquals(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    @SuppressWarnings("unchecked")
    private void resizeIfThresholdReached() {
        if (!(size == threshold)) {
            return;
        }
        capacity *= GROW_FACTOR;
        threshold *= GROW_FACTOR;
        Node<K, V>[] oldStorage = elementsStorage;
        elementsStorage = new Node[capacity];
        Node<K, V> currentNode;
        size = 0;
        for (Node<K, V> kvNode : oldStorage) {
            currentNode = kvNode;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
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
