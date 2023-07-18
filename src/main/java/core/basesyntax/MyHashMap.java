package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_MULTIPLIER = 2;
    private int threshold;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key),key, value);
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int hash = hash(key);
        int index = getBucketIndex(hash);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.hash == hash)
                    && (node.key == key || node.key != null && node.key.equals(key))) {
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

    private V putValue(int hash, K key, V value) {
        Node<K,V> node = new Node<>(hash, key, value, null);
        int index = getBucketIndex(hash);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            addIfBucketIsNotEmpty(hash, node);
        }
        if (size > threshold) {
            resize();
        }
        return value;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getBucketIndex(int hash) {
        return Math.abs(hash % table.length);
    }

    private V addIfBucketIsNotEmpty(int hash, Node<K, V> newNode) {
        int index = getBucketIndex(hash);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((newNode.key == node.key)
                    || (newNode.key != null && newNode.key.equals(node.key))) {
                node.value = newNode.value;
                return newNode.value;
            }
            if (node.next == null) {
                break;
            }
            node = node.next;

        }
        node.next = newNode;
        size++;
        return newNode.value;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
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
