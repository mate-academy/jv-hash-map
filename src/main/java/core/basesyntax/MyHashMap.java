package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_GROW = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
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
        int hash = hash(key);
        if (size == threshold()) {
            resize();
        }
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = new Node<>(hash, key, value, null);
            size++;
        } else {
            while (node != null) {
                if (equalKey(node, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(hash, key, value, null);
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    private void resize() {
        size = 0;
        Node<K,V> [] oldTable = table;
        table = (Node<K,V>[]) new Node[capacity() * CAPACITY_GROW];
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
        while (node != null) {
            if (equalKey(node, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private boolean equalKey(Node<K,V> node, K key) {
        if (key == node.key || key != null && key.equals(node.key)) {
            return true;
        }
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int capacity() {
        return table.length;
    }
}
