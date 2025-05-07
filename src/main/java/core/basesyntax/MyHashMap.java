package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getKeyIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (equalsKey(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return (node == null) ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private Node<K, V> getNode(K key) {
        int index = getKeyIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (equalsKey(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private boolean equalsKey(K k1, K k2) {
        return (k1 == null && k2 == null) || (k1 != null && k1.equals(k2));
    }

    private int getKeyIndex(K key) {
        return (key == null) ? 0 : Math.abs(hash(key) % table.length);
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                addNode(node.key, node.value, getKeyIndex(node.key));
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
