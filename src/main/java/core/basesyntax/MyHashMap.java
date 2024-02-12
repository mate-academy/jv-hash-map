package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = new Node<>(key, value, null);
        } else {
            Node<K, V> lastNode = null;
            do {
                if (node.key == key || key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    lastNode = node;
                }
                node = node.next;
            } while (node != null);
            lastNode.next = new Node<>(key, value, null);
        }
        if (++size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
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

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        @SuppressWarnings("unchecked")
        Node<K, V>[] newTable = (Node<K,V>[]) new Node[table.length * GROW_FACTOR];
        table = newTable;
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K,V>[] oldTable) {
        for (int j = 0; j < oldTable.length; j++) {
            Node<K, V> node = oldTable[j];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
