package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private int capacity;
    private int threshold;
    private float loadFactor;
    private Node<K, V>[] buckets;
    private int size;

    public <K, V> MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int) (capacity * loadFactor);
        buckets = new Node[capacity];
        size = 0;
    }

    public <K, V> MyHashMap(int capacity) {
        this.capacity = Math.min(capacity, MAXIMUM_CAPACITY);
        loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int) (capacity * loadFactor);
        buckets = new Node[capacity];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value);
        } else {
            Node<K, V> prevNode = null;
            Node<K, V> currentNode = buckets[index];
            for (; currentNode != null;
                    prevNode = currentNode, currentNode = currentNode.next) {
                if (isKeysEquals(currentNode, key)) {
                    currentNode.value = value;
                    return;
                }
            }
            prevNode.next = new Node<>(key, value);
        }
        size++;
        checkResize();
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> currentNode = buckets[index];
                        currentNode != null;
                        currentNode = currentNode.next) {
            if (isKeysEquals(currentNode, key)) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean isKeysEquals(Node<K, V> currentNode, K key) {
        // currentNode must be != null
        return ((key == currentNode.key) // including (key == currentNode.key == null)
                || ((key != null)
                && ((key.hashCode() == currentNode.hash)
                && key.equals(currentNode.key))));
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void checkResize() {
        if (size < threshold || capacity == MAXIMUM_CAPACITY) {
            return;
        }
        int newCapacity = capacity << 1;
        MyHashMap<K, V> newMap = new MyHashMap<>(newCapacity);
        int itemCounter = 0;
        for (int i = 0; (i < capacity) && (itemCounter < size); i++) {
            for (Node<K, V> currentNode = buckets[i];
                            currentNode != null;
                            currentNode = currentNode.next) {
                newMap.put(currentNode.key, currentNode.value);
                itemCounter++;
            }
        }
        // this = newMap;
        capacity = newMap.capacity;
        threshold = newMap.threshold;
        loadFactor = newMap.loadFactor;
        buckets = newMap.buckets;
        size = newMap.size;
    }

    private class Node<K, V> {
        private final int hash; // hash code of the key
        private final K key;
        private V value;
        private Node<K, V> next; // link to the next node

        Node(K key, V value) {
            hash = (key == null) ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}
