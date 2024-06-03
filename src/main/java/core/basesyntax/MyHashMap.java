package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] buckets;
    private int size;
    private int capacity;
    private Node<K, V> nullKeyNode; // Special node to handle the null key

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new Node[capacity];
        this.size = 0;
        this.nullKeyNode = null; // Initialize the special null key node to null
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (nullKeyNode == null) {
                nullKeyNode = new Node<>(key, value, null);
                size++;
            } else {
                nullKeyNode.setValue(value);
            }
            return;
        }

        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> current = buckets[index];
            Node<K, V> prev = null;
            while (current != null) {
                if (current.getKey().equals(key)) {
                    current.setValue(value);
                    return;
                }
                prev = current;
                current = current.getNext();
            }
            if (prev == null) {
                buckets[index] = newNode;
            } else {
                prev.setNext(newNode);
            }
        }
        size++;
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyNode == null ? null : nullKeyNode.getValue();
        }

        int index = hash(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (current.getKey().equals(key)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean containsKey(K key) {
        if (key == null) {
            return nullKeyNode != null;
        }

        int index = hash(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (current.getKey().equals(key)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    public V remove(K key) {
        if (key == null) {
            if (nullKeyNode != null) {
                V value = nullKeyNode.getValue();
                nullKeyNode = null;
                size--;
                return value;
            }
            return null;
        }

        int index = hash(key);
        Node<K, V> current = buckets[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (current.getKey().equals(key)) {
                if (prev == null) {
                    buckets[index] = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                size--;
                return current.getValue();
            }
            prev = current;
            current = current.getNext();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        Node<K, V>[] newBuckets = new Node[capacity];

        for (Node<K, V> bucket : buckets) {
            Node<K, V> current = bucket;
            while (current != null) {
                int newIndex = hash(current.getKey());
                Node<K, V> next = current.getNext();
                current.setNext(newBuckets[newIndex]);
                newBuckets[newIndex] = current;
                current = next;
            }
        }
        buckets = newBuckets;
    }
}
