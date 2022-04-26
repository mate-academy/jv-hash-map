package core.basesyntax;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_ARRAY_INCREASE = 2;
    private static final float LOAD_FACTORY = 0.75f;
    private Node<K, V>[] bucketsArray;
    private int size;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        putInBucketArray(getHash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> node = bucketsArray[getIndex(getHash(key))];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newArraySize = (bucketsArray == null) ? DEFAULT_CAPACITY : bucketsArray.length
                * DEFAULT_ARRAY_INCREASE;
        if (size == 0) {
            bucketsArray = (Node<K, V>[]) new Node[newArraySize];
            return;
        }
        if ((float)size / bucketsArray.length >= LOAD_FACTORY) {
            Node<K, V>[] oldBucketsArray = bucketsArray;
            bucketsArray = (Node<K, V>[]) new Node[newArraySize];
            size = 0;
            for (Node<K, V> node : oldBucketsArray) {
                if (node != null) {
                    do {
                        putInBucketArray(node.hash, node.key, node.value);
                        node = node.next;
                    } while (node != null);
                }
            }
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(int hash) {
        return hash % bucketsArray.length;
    }

    private void putInBucketArray(int hash, K key, V value) {
        int index = getIndex(hash);
        if (bucketsArray[index] == null) {
            bucketsArray[index] = new Node<>(hash, key, value);
            size++;
            return;
        }
        Node<K, V> bucketNode = bucketsArray[index];
        while (bucketNode != null) {
            if (key == bucketNode.key
                    || key != null && key.equals(bucketNode.key)) {
                bucketNode.value = value;
                return;
            }
            if (bucketNode.next == null) {
                bucketNode.next = new Node<>(hash, key, value);
                size++;
                return;
            }
            bucketNode = bucketNode.next;
        }
    }
}
