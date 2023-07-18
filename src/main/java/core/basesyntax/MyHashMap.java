package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K,V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        putValue(hash(key),key, value);
    }

    @Override
    public V getValue(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int index = bucketIndex(hash(key));
        Node<K, V> node = table[index];
        while (key == null && node.key != null || node.key == null
                && key != null || node.key != null && !node.key.equals(key)) {
            node = node.next;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
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

    private V putValue(int hash, K key, V value) {
        Node<K,V> node = new Node<>(hash, key, value, null);
        if (table == null || table.length == 0) {
            table = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        int index = bucketIndex(hash);
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
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

    private boolean containsKey(K key) {
        return (table != null && table.length != 0)
                && (table[bucketIndex(hash(key))] != null || key == null);
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int bucketIndex(int hash) {
        return Math.abs(hash % table.length);
    }

    private V addIfBucketIsNotEmpty(int hash, Node<K, V> newNode) {
        int index = bucketIndex(hash);
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
}
