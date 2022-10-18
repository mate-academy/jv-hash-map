package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int SCALING_NUMBER = 2;
    private Object[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Object[DEFAULT_CAPACITY];
        size = size;
        threshold = (int) LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int hash = hashCode(key);
        int indexNode = hash % table.length;
        Node<K, V> node = (Node<K, V>) table[indexNode];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(hash, key, value, null);
                size++;
                if (size > threshold) {
                    table = resize();
                }
                return;
            }
            node = node.next;
        }
        table[indexNode] = new Node<>(hash, key, value, null);
        size++;
        if (size > threshold) {
            table = resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNodeByKey(key);

        return (node != null) ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashCode(K key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        return (hashCode < 0) ? -hashCode : hashCode;
    }

    private Object[] resize() {
        Object[] newTable = new Object[table.length * SCALING_NUMBER];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = (Node<K, V>) table[i];
            while (node != null) {
                int index = node.hash % newTable.length;
                Node<K, V> otherNode = node.next;
                node.next = null;
                Node<K, V> newTableNode = (Node<K, V>) newTable[index];
                if (newTableNode == null) {
                    newTable[index] = node;
                } else {
                    while (newTableNode.next != null) {
                        newTableNode = newTableNode.next;
                    }
                    newTableNode.next = node;
                }
                node = otherNode;
            }
        }
        threshold = (int) (newTable.length * LOAD_FACTOR);
        return newTable;
    }

    private Node<K, V> getNodeByKey(K key) {
        int hash = hashCode(key);
        Node<K, V> node = (Node<K, V>) table[hash % table.length];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
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
