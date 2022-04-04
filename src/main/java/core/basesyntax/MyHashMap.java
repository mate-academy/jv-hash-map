package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] buckets;

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(getHashCode(key), key, value, null);
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = newNode;
            size++;
        } else {
            Node<K, V> temp = buckets[index];
            while (temp != null) {
                if (checkKeys(key, temp.key)) {
                    temp.value = value;
                    break;
                } else if (temp.next == null) {
                    temp.next = newNode;
                    size++;
                    break;
                } else {
                    temp = temp.next;
                }
            }
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> temp = buckets[index]; temp != null; temp = temp.next) {
            if (checkKeys(key, temp.key)) {
                return temp.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return key == null ? 0 : getHashCode(key) % capacity;
    }

    private void transfer(Node<K, V>[] oldBuckets) {
        for (Node<K, V> enrty : oldBuckets) {
            while (enrty != null) {
                put(enrty.key, enrty.value);
                enrty = enrty.next;
            }
        }
    }

    private void resize() {
        if (size == threshold) {
            capacity = capacity * 2;
            final Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[capacity];
            size = 0;
            threshold = (int) (capacity * LOAD_FACTOR);
            transfer(oldBuckets);
        }
    }

    private boolean checkKeys(K firstKey, K secondKey) {
        return firstKey == secondKey
                || firstKey != null && firstKey.equals(secondKey);
    }
}
