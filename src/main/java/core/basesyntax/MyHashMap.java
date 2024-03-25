package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] buckets;

    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (buckets[0] == null) {
                buckets[0] = new Node<>(null, value);
            } else {
                Node<K, V> current = buckets[0];
                while (current != null) {
                    if (current.key == null) {
                        current.value = value;
                        return;
                    }
                    if (current.next == null) {
                        break;
                    }
                    current = current.next;
                }
                current.next = new Node<>(null, value);
            }
            size++;

            if ((double) size / buckets.length > LOAD_FACTOR) {
                resize();
            }
            return;
        }

        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> current = buckets[index];
            while (current != null) {
                if (current.key != null && current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = newNode;
        }

        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (current.key != null && current.key.equals(key)) {
                return current.value;
            }
            if (current.key == null && key == null) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];
        int newSize = -1;

        for (Node<K, V> bucket : buckets) {
            Node<K, V> current = bucket;
            while (current != null) {
                int index = getIndex(current.key);
                Node<K, V> newNode = new Node<>(current.key, current.value);
                if (newBuckets[index] == null) {
                    newBuckets[index] = newNode;
                } else {
                    Node<K, V> temp = newBuckets[index];
                    while (temp.next != null) {
                        temp = temp.next;
                    }
                    temp.next = newNode;
                }
                current = current.next;
                newSize++;
            }
        }
        buckets = newBuckets;
        size = newSize;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

}
