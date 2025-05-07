package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K,V>[] bucket;

    public MyHashMap() {
        bucket = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == (int) (bucket.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }

        int indexOnHash = hashKey(key);
        Node<K,V> prevTable = null;
        Node<K, V> node = bucket[indexOnHash];
        Node<K,V> newTable = new Node<>(key, value,null);

        if (node == null) {
            bucket[indexOnHash] = newTable;
        } else {
            while (node != null) {
                if (node.key == key
                        || node.key != null && node.key.equals(key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    prevTable = node;
                }
                node = node.next;
            }
            prevTable.next = newTable;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexOnHash = hashKey(key);
        Node<K, V> node = bucket[indexOnHash];
        while (node != null) {
            if (node.key == key
                    || node.key != null && node.key.equals(key)) {
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

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int hashKey(K key) {
        return (key == null) ? 0 : ((int) Math.abs(key.hashCode() % (bucket.length)));
    }

    private Node<K,V>[] resize() {
        size = 0;
        int capacity = bucket.length << 1;
        Node<K,V>[] oldBuckets = bucket;
        Node<K,V> oldNode;
        bucket = (Node<K, V>[]) new Node[capacity];
        for (Node<K,V> oldBucket : oldBuckets) {
            oldNode = oldBucket;
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
        return bucket;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
