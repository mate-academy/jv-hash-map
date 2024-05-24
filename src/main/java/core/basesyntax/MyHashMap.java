package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.getNext()) {
            if (node.getKey() == key || (key != null && key.equals(node.getKey()))) {
                node.setValue(value);
                return;
            }
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.getNext()) {
            if (node.getKey() == key || (key != null && key.equals(node.getKey()))) {
                return node.getValue();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        int hash = (key == null) ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
        return hash & (table.length - 1);
    }

    private void addNode(K key, V value, int bucketIndex) {
        Node<K, V> node = table[bucketIndex];
        table[bucketIndex] = new Node<>(key, value, node);
        if (++size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.getNext();
                int index = getBucketIndexForNewTable(node.getKey(), newTable.length);
                node.setNext(newTable[index]);
                newTable[index] = node;
                node = next;
            }
        }
    }

    private int getBucketIndexForNewTable(K key, int newTableLength) {
        int hash = (key == null) ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
        return hash & (newTableLength - 1);
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V newValue) {
            value = newValue;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
