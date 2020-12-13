package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity = 16;
    private Node<K, V>[] table = (Node<K, V>[]) new Node[capacity];
    private int size;

    @Override
    public void put(K key, V value) {
        Node<K, V> node;
        if ((node = getNode(hash(key), key)) != null) {
            node.value = value;
            return;
        }
        putValue(hash(key), key, value);
    }

    private void putValue(int hash, K key, V value) {
        Node<K, V> previous = table[getIndex(hash)];
        if (previous == null) {
            table[getIndex(hash)] = new Node<>(hash, key, value, null);
        } else {
            while (previous.next != null) {
                previous = previous.next;
            }
            previous.next = new Node<>(hash, key, value, null);
        }
        if (++size > getThreshold()) {
            table = resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(hash(key), key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        capacity = capacity * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        transfer(newTable);
        return newTable;
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] tmp = table;
        table = newTable;
        for (Node<K, V> node : tmp) {
            while (node != null) {
                put(node.key, node.value);
                size--;
                node = node.next;
            }
        }
    }

    private int getThreshold() {
        return (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    private Node<K, V> getNode(int hash, K key) {
        Node<K, V> node = table[getIndex(hash)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                break;
            }
            node = node.next;
        }
        return node;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int getIndex(int hash) {
        return (table.length - 1) & hash;
    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
