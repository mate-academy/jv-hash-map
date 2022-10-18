package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int HASHCODE_FOR_NULL_VALUE = 0;
    private static final int DEFAULT_SCALING_NUMBER = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_SIZE = 0;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int threshold;
    private int size;

    public MyHashMap() {
        thresholdCounter();
    }

    @Override
    public void put(K key, V value) {
        int bucket = hash(key);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if ((node.key == key) || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            } else if (node.next != null) {
                node = node.next;
            } else {
                break;
            }
        }
        if (table[bucket] == null) {
            table[bucket] = new Node<>(key,value);
        } else {
            node.next = new Node<>(key,value);
        }
        if (++size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = hash(key);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if ((node.key == key) || (node.key != null && node.key.equals(key))) {
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

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.hash = hash(key);
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = new Node[table.length * DEFAULT_SCALING_NUMBER];
        thresholdCounter();
        size = DEFAULT_SIZE;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(Object key) {
        return key == null ? HASHCODE_FOR_NULL_VALUE : Math.abs(key.hashCode() % table.length);
    }

    private void thresholdCounter() {
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }
}
