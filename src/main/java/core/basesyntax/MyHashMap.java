package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold;
    private int size;
    private Node<K,V>[] bucket;

    public MyHashMap() {
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        bucket = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }

        int indexOnHash = hashKey(key);
        Node<K,V> prevTable = null;
        Node<K, V> table = bucket[indexOnHash];
        Node<K,V> newTable = new Node<>(key, value,null);

        if (table == null) {
            bucket[indexOnHash] = newTable;
        } else {
            while (table != null) {
                if (table.key == key
                        || table.key != null && table.key.equals(key)) {
                    table.value = value;
                    return;
                }
                if (table.next == null) {
                    prevTable = table;
                }
                table = table.next;
            }
            prevTable.next = newTable;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> prevTable = null;
        int indexOnHash = hashKey(key);
        Node<K, V> table = bucket[indexOnHash];
        if (table == null) {
            return null;
        }
        while (table.next != null) {
            if (table.key == key
                    || table.key != null && table.key.equals(key)) {
                return table.value;
            }
            prevTable = table;
            table = table.next;
        }
        if (table.next == null && prevTable == null) {
            return bucket[indexOnHash].value;
        } else {
            return prevTable.next.value;
        }
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
        return (key == null) ? 0 : ((int) Math.abs(key.hashCode() % capacity));
    }

    private Node<K,V>[] resize() {
        size = 0;
        capacity = capacity << 1;
        threshold = threshold << 1;
        Node<K,V>[] oldBuckets = bucket;
        Node<K,V> oldTable;
        bucket = (Node<K, V>[]) new Node[capacity];
        for (Node<K,V> oldBucket: oldBuckets) {
            oldTable = oldBucket;
            while (oldTable != null) {
                put(oldTable.key, oldTable.value);
                oldTable = oldTable.next;
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
