package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи`put(K key,V value)`,`getValue()` та`getsizeOfStock()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resizeOfStock...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] bucketStock;
    private int size;

    public <K, V> MyHashMap() {
        bucketStock = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int index = getIndexFromHash(getHash(key.hashCode()));
        if (bucketStock[index] == null) {
            bucketStock[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> lastBucketNode = bucketStock[index];
            if (lastBucketNode.key.equals(key)) {
                lastBucketNode.value = value;
                return;
            }
            while (lastBucketNode.nextNode != null) {
                if (lastBucketNode.key.equals(key)) {
                    lastBucketNode.value = value;
                    return;
                }
                lastBucketNode = lastBucketNode.nextNode;
            }
            lastBucketNode.nextNode = new Node<>(key, value, null);
        }
        size++;
    }

    private void putForNullKey(V value) {
        if (bucketStock[0] == null) {
            bucketStock[0] = new Node<>(null, value, null);
        } else {
            Node<K, V> lastBucketNode = bucketStock[0];
            if (lastBucketNode.key == null) {
                lastBucketNode.value = value;
                return;
            }
            while (lastBucketNode.nextNode != null) {
                if (lastBucketNode.key == null) {
                    lastBucketNode.value = value;
                    return;
                }
                lastBucketNode = lastBucketNode.nextNode;
            }
            lastBucketNode.nextNode = new Node<K, V>(null, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }

        int index = getIndexFromHash(getHash(key.hashCode()));
        if (index >= bucketStock.length || bucketStock[index] == null) {
            return null;
        }
        Node<K, V> returnNode = bucketStock[index];
        while (returnNode != null) {
            if (returnNode.key.equals(key)) {
                return returnNode.value;
            }
            returnNode = returnNode.nextNode;
        }
        return null;
    }

    private V getForNullKey() {
        Node<K, V> returnNode = bucketStock[0];
        while (returnNode != null) {
            if (returnNode.key == null) {
                return returnNode.value;
            }
            returnNode = returnNode.nextNode;
        }
        return null;
    }

    private int getHash(int hashCode) {
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);

    }

    private int getIndexFromHash(int hash) {
        return Math.abs(hash % bucketStock.length);
    }

    private void resize() {
        if (size >= bucketStock.length * LOAD_FACTOR) {
            Node<K, V>[] oldBucketStock = bucketStock;
            bucketStock = new Node[bucketStock.length * 2];
            size = 0;
            for (int i = 0; i < oldBucketStock.length; i++) {
                if (oldBucketStock[i] == null) {
                    continue;
                }
                while (oldBucketStock[i] != null) {
                    put(oldBucketStock[i].key, oldBucketStock[i].value);
                    oldBucketStock[i] = oldBucketStock[i].nextNode;
                }
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
