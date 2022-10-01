package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Object[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Object[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
        this.threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY;
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = hashCode(key);
        int nodeIndex = hash % table.length;
        Node<K,V> node = (Node<K, V>) table[nodeIndex];
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
        table[nodeIndex] = new Node<>(hash, key, value, null);
        size++;
        if (size > threshold) {
            table = resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = getNodeByKey(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int hashCode(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        return hashCode < 0 ? -hashCode : hashCode;
    }

    private Object[] resize() {
        Object[] newTable = new Object[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = (Node<K, V>) table[i];
            while (node != null) {
                int indexInNewTable = node.hash % newTable.length;
                Node<K, V> nextNode = node.next;
                node.next = null;
                Node<K, V> nodeInNewTable = (Node<K, V>) newTable[indexInNewTable];
                if (nodeInNewTable == null) {
                    newTable[indexInNewTable] = node;
                } else {
                    while (nodeInNewTable.next != null) {
                        nodeInNewTable = nodeInNewTable.next;
                    }
                    nodeInNewTable.next = node;
                }
                node = nextNode;
            }
        }
        threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
        return newTable;
    }

    private Node<K,V> getNodeByKey(K key) {
        int hash = hashCode(key);
        Node<K,V> node = (Node<K, V>) table[hash % table.length];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }
}
