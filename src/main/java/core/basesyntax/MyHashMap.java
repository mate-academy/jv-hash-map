package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private int size;

    private Node<K, V>[] bucketArray;

    public MyHashMap() {
        bucketArray = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            handleNullKey(value);
            return;
        }

        int bucketIndex = getBucketIndex(key);
        if (bucketArray[bucketIndex] == null) {
            bucketArray[bucketIndex] = new Node<>(key, value);
        } else {
            Node<K, V> current = bucketArray[bucketIndex];
            while (current != null) {
                if (key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = new Node<>(key, value);
        }

        size++;
        sizeCheck();
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return handleNullKey();
        }

        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = bucketArray[bucketIndex];
        while (current != null) {
            if (key.equals(current.key)) {
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

    private void handleNullKey(V value) {
        if (bucketArray[0] == null) {
            bucketArray[0] = new Node<>(null, value); // Add a new node with null key
            size++;
            if ((double) size / bucketArray.length > LOAD_FACTOR) {
                resize();
            }
            return;
        }

        Node<K, V> current = bucketArray[0];
        while (true) {
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
        size++;
    }

    private V handleNullKey() {
        Node<K, V> current = bucketArray[0];
        while (current != null) {
            if (current.key == null) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % bucketArray.length);
    }

    private void sizeCheck() {
        if ((double) size / bucketArray.length > LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = bucketArray.length * 2;
        Node<K, V>[] newBucketArray = new Node[newCapacity];

        for (Node<K, V> kvNode : bucketArray) {
            Node<K, V> current = kvNode;
            while (current != null) {
                int newBucketIndex = (current.key == null)
                        ? 0 : Math.abs(current.key.hashCode() % newCapacity);
                if (newBucketArray[newBucketIndex] == null) {
                    newBucketArray[newBucketIndex] = new Node<>(current.key, current.value);
                } else {
                    Node<K, V> newCurrent = newBucketArray[newBucketIndex];
                    while (newCurrent.next != null) {
                        newCurrent = newCurrent.next;
                    }
                    newCurrent.next = new Node<>(current.key, current.value);
                }
                current = current.next;
            }
        }

        bucketArray = newBucketArray;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.hash = (key == null) ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
