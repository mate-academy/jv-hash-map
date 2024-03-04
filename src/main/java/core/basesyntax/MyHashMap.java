package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private static final int ZERO_SIZE = 0;
    private int threshold;
    private Node<K, V>[] elementsStorage;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        elementsStorage = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfThresholdReached();
        int bucketIndex = calculateIndex(key);
        Node<K, V> node = new Node<>(key, value, null);
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
        int bucketIndex = calculateIndex(key);
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

    private int calculateIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return Math.abs(hash) % elementsStorage.length;
    }

    private boolean isKeysEquals(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    @SuppressWarnings("unchecked")
    private void resizeIfThresholdReached() {
        if (size != threshold) {
            return;
        }
        threshold *= GROW_FACTOR;
        int newCapacity = elementsStorage.length * GROW_FACTOR;
        Node<K, V>[] oldStorage = elementsStorage;
        elementsStorage = new Node[newCapacity];
        Node<K, V> currentNode;
        size = ZERO_SIZE;
        for (Node<K, V> node : oldStorage) {
            currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
