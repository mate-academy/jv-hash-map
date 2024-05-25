package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.getNext()) {
            if ((node.getKey() == key) || (key != null && key.equals(node.getKey()))) {
                node.setValue(value);
                return;
            }
        }
        addMyNode(key, value, index);
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

    public void addMyNode(K key, V value, int index) {
        Node<K, V> firstNode = table[index];
        table[index] = new Node<>(key, value, firstNode);
        if (++size > (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
    }

    public void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.getNext();
                int index = getNewBucketIndex(currentNode.getKey(), newTable.length);
                currentNode.setNext(newTable[index]);
                newTable[index] = currentNode;
                currentNode = nextNode;
            }
        }
    }

    private int getNewBucketIndex(K key, int newTableLength) {
        int hash = (key == null) ? 0 : key.hashCode();
        return (hash & 0x7fffffff) % newTableLength;
    }

    private int getBucketIndex(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return (hash & 0x7fffffff) % table.length;
    }

    public void resize() {
        int newCapacity = table.length * CAPACITY_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K,V> next) {
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
