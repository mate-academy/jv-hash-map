package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int capacity) {
        this.buckets = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putNullKey(value);
            return;
        }
        int index = getIndex(key);
        Node<K, V> entry = buckets[index];
        Node<K, V> prev = findNodeByKey(entry, key);
        if (prev != null) {
            prev.setValue(value);
        } else {
            Node<K, V> newEntry = new Node<>(key, value);
            addToBucket(newEntry, index);
            size++;
        }
        resizeIfNeeded();
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getValueForNullKey();
        }
        int index = getIndex(key);
        Node<K, V> node = findNodeByKey(buckets[index], key);
        return (node != null) ? node.getValue() : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNullKey(V value) {
        int index = getNullKeyIndex();
        Node<K, V> entry = buckets[index];
        Node<K, V> prev = findNullKeyNode(entry);
        if (prev != null) {
            prev.setValue(value);
        } else {
            Node<K, V> newEntry = new Node<>(null, value);
            addToBucket(newEntry, index);
            size++;
        }
        resizeIfNeeded();
    }

    private Node<K, V> findNodeByKey(Node<K, V> entry, K key) {
        while (entry != null) {
            if (entry.getKey() == null || entry.getKey().equals(key)) {
                return entry;
            }
            entry = entry.getNext();
        }
        return null;
    }

    private Node<K, V> findNullKeyNode(Node<K, V> entry) {
        while (entry != null) {
            if (entry.getKey() == null) {
                return entry;
            }
            entry = entry.getNext();
        }
        return null;
    }

    private void addToBucket(Node<K, V> newEntry, int index) {
        Node<K, V> entry = buckets[index];
        if (entry != null) {
            while (entry.getNext() != null) {
                entry = entry.getNext();
            }
            entry.setNext(newEntry);
        } else {
            buckets[index] = newEntry;
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return getNullKeyIndex();
        }
        int hashCode = key.hashCode();
        int index = hashCode % buckets.length;
        return index < 0 ? index + buckets.length : index;
    }

    private int getIndex(K key, int capacity) {
        int hashCode = key.hashCode();
        int index = hashCode % capacity;
        return index < 0 ? index + capacity : index;
    }

    private int getNullKeyIndex() {
        return buckets.length - 1;
    }

    private void resizeIfNeeded() {
        if ((float) size / buckets.length >= DEFAULT_LOAD_FACTOR) {
            int newCapacity = buckets.length * 2;
            Node<K, V>[] newBuckets = new Node[newCapacity];
            for (Node<K, V> node : buckets) {
                while (node != null) {
                    Node<K, V> next = node.getNext();
                    int newIndex = getIndex(node.getKey(), newCapacity);
                    node.setNext(newBuckets[newIndex]);
                    newBuckets[newIndex] = node;
                    node = next;
                }
            }
            buckets = newBuckets;
        }
    }

    private V getValueForNullKey() {
        int index = getNullKeyIndex();
        Node<K, V> entry = buckets[index];
        Node<K, V> nullKeyNode = findNullKeyNode(entry);
        return (nullKeyNode != null) ? nullKeyNode.getValue() : null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
