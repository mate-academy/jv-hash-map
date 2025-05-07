package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final int CAPACITY_GROW = 2;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V> [] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity();
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    private void putVal(int hash, K key, V value) {
        if (size == threshold()) {
            resize();
        }
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = new Node<>(hash, key, value, null);
        } else {
            while (node.next != null) {
                if (key == node.key || (key != null && key.equals(node.key))) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (key == node.key || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
            node.next = new Node<>(hash, key, value, null);
        }
        size++;
    }

    private void resize() {
        size = 0;
        Node<K,V> [] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity() * CAPACITY_GROW];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int threshold() {
        return (int)(capacity() * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[hash(key)];
        if (node == null) {
            return null;
        } else {
            do {
                if (key == node.key || (key != null && key.equals(node.key))) {
                    return node.value;
                }
            } while ((node = node.next) != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int capacity() {
        return table.length;
    }
}
