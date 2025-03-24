package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] buckets;

    private final float loadFactor = 0.75f;
    private int threshold;
    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (buckets == null || buckets.length == 0 || size == threshold) {
            buckets = resize();
        }
        if (key == null) {
            if (buckets[0] == null) {
                buckets[0] = new Node<>(0, null, value, null);
            } else if (buckets[0].key == null) {
                buckets[0].value = value;
                return;
            } else {
                Node<K, V> currentNode = buckets[0];
                while (currentNode.next != null) {
                    if (currentNode.key == null) {
                        currentNode.value = value;
                        return;
                    }
                    currentNode = currentNode.next;
                }
                currentNode.next = new Node<>(0, null, value, null);
            }
            size++;
            return;
        }
        int index = key.hashCode() & (buckets.length - 1);
        if (buckets[index] == null) {
            buckets[index] = new Node<>(key.hashCode(), key, value, null);
            size++;
        } else if (buckets[index].key.equals(key)) {
            buckets[index].value = value;
        } else {
            Node<K, V> currentNode = buckets[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if (key.equals(currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
            }
            currentNode.next = new Node<>(key.hashCode(), key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (buckets == null || buckets.length == 0) {
            return null;
        }
        if (key == null) {
            Node<K, V> currentNode = buckets[0];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }
        int index = key.hashCode() & (buckets.length - 1);
        if (buckets[index] == null) {
            return null;
        } else if (key.equals(buckets[index].key)) {
            return buckets[index].value;
        }
        Node<K, V> currentNode = buckets[index];
        while (currentNode.next != null) {
            currentNode = currentNode.next;
            if (key.equals(currentNode.key)) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        int oldCapacity;
        int newCapacity;
        if (buckets == null) {
            oldCapacity = 0;
        } else {
            oldCapacity = buckets.length;
        }
        if (oldCapacity == 0) {
            newCapacity = 16;
            threshold = (int) (newCapacity * loadFactor);
            return new Node[newCapacity];
        } else {
            newCapacity = oldCapacity * 2;
            threshold = (int) (newCapacity * loadFactor);
            return transfer(newCapacity);
        }
    }

    private Node<K, V>[] transfer(int newCapacity) {
        Node<K, V>[] newBuckets = new Node[newCapacity];
        for (Node<K,V> bucket : buckets) {
            if (bucket != null) {
                while (bucket != null) {
                    Node<K,V> bucketNext = bucket.next;
                    bucket.next = null;
                    int index = bucket.hash & (newCapacity - 1);
                    if (newBuckets[index] == null) {
                        newBuckets[index] = bucket;
                    } else {
                        Node<K,V> currentNewBucket = newBuckets[index];
                        while (currentNewBucket.next != null) {
                            currentNewBucket = currentNewBucket.next;
                        }
                        currentNewBucket.next = bucket;
                    }
                    bucket = bucketNext;
                }
            }
        }
        return newBuckets;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{"
                    + "hash=" + hash
                    + ", key=" + key
                    + ", value=" + value
                    + ", next=" + next
                    + '}';
        }
    }
}
