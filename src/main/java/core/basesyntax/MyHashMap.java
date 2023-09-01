package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = createNodeArray(DEFAULT_CAPACITY);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putValueForNullKey(value);
        } else {
            int hash = hash(key);
            int index = indexFor(hash, buckets.length);
            Node<K, V> newNode = new Node<>(hash, key, value, null);
            if (buckets[index] == null) {
                buckets[index] = newNode;
                size++;
            } else {
                Node<K, V> currentNode = buckets[index];
                Node<K, V> prevNode = null;
                while (currentNode != null) {
                    if (key.equals(currentNode.key)) {
                        currentNode.value = value;
                        return;
                    }
                    prevNode = currentNode;
                    currentNode = currentNode.next;
                }
                if (prevNode != null) {
                    Node<K, V> newNextNode = prevNode.next;
                    prevNode.next = newNode;
                    newNode.next = newNextNode;
                    size++;
                }
            }
            if ((float) size / buckets.length >= LOAD_FACTOR) {
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getValueForNullKey();
        } else {
            int hash = hash(key);
            int index = indexFor(hash, buckets.length);
            Node<K, V> currentNode = buckets[index];
            while (currentNode != null) {
                if (key.equals(currentNode.key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }
    }

    public int getSize() {
        return size;
    }

    private void putValueForNullKey(V value) {
        if (buckets[0] == null) {
            buckets[0] = new Node<>(0, null, value, null);
            size++;
        } else {
            Node<K, V> currentNode = buckets[0];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            Node<K, V> newNode = new Node<>(0, null, value, buckets[0]);
            buckets[0] = newNode;
            size++;
        }
    }

    private V getValueForNullKey() {
        if (buckets[0] == null) {
            return null;
        } else {
            Node<K, V> currentNode = buckets[0];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    private int indexFor(int hash, int length) {
        return (hash & 0x7FFFFFFF) % length;
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = createNodeArray(newCapacity);
        int newSize = 0;

        for (Node<K, V> node : buckets) {
            while (node != null) {
                Node<K, V> nextNode = node.next;
                int newIndex = indexFor(node.hash, newCapacity);
                node.next = newBuckets[newIndex];
                newBuckets[newIndex] = node;
                node = nextNode;
                newSize++;
            }
        }

        buckets = newBuckets;
        size = newSize;
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] createNodeArray(int size) {
        return (Node<K, V>[]) new Node[size];
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
