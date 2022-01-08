package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private double loadFactor = DEFAULT_LOAD_FACTOR;
    private int size;

    private Object[] table;

    private static class Node<K1, V1> {
        private final K1 key;
        private V1 value;
        private final int hash;
        private Node<K1,V1> next;

        private Node(K1 key, V1 value) {
            this.key = key;
            this.hash = key == null ? 0 : key.hashCode();
            this.value = value;
        }

        private boolean isKey(K1 key) {
            return this.key == null ? key == null : this.key.equals(key);
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> node = getNode(key);
        if (node != null) {
            node.value = value;
            return;
        }
        if (table == null) {
            table = new Object[DEFAULT_INITIAL_CAPACITY];
        } else {
            int threshold = (int) (table.length * loadFactor);
            if (size >= threshold) {
                resize();
            }
        }
        putHelper(new Node<K,V>(key, value), table);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = getNode(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V> getNode(K key) {
        if (table == null) {
            return null;
        }
        if (table.length == 0) {
            return null;
        }
        int hash =  key == null ? 0 : key.hashCode();
        int bucketNo = getBucketNo(hash, table);
        Node<K,V> node = (Node<K,V>) table[bucketNo];
        while (node != null) {
            if (node.isKey(key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void resize() {
        Object[] newTable = new Object[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<K,V> node = (Node<K,V>) table[i];
            while (node != null) {
                putHelper(new Node(node.key, node.value), newTable);
                node = node.next;
            }
        }
        table = newTable;
    }

    private void putHelper(Node<K,V> node, Object[] array) {
        int bucketNo = getBucketNo(node.hash, array);
        Node<K,V> bucketNode = (Node<K,V>) array[bucketNo];
        if (bucketNode == null) {
            array[bucketNo] = node;
            return;
        }
        while (bucketNode.next != null) {
            bucketNode = bucketNode.next;
        }
        bucketNode.next = node;
    }

    private int getBucketNo(int hash, Object[] buckets) {
        int bucketNo = hash % buckets.length;
        return bucketNo < 0 ? - bucketNo : bucketNo;
    }
}
