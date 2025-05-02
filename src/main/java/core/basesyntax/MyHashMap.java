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
        int index = getNewBucketIndex(key, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        size++;
        resize();
        index = getNewBucketIndex(key, table.length);
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = getNewBucketIndex(key, table.length);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.key == key || key != null && key.equals(node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> firstNode = table[index];
        table[index] = new Node<>(key, value, firstNode);
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> currentNode : table) {
            for (Node<K, V> node = currentNode; node != null;) {
                Node<K, V> nextNode = node.next;
                int index = getNewBucketIndex(node.key, newTable.length);
                node.next = newTable[index];
                newTable[index] = node;
                node = nextNode;
            }
        }
    }

    private int getNewBucketIndex(K key, int tableLength) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % tableLength);
    }

    private void resize() {
        if (size > (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            int newCapacity = table.length * CAPACITY_MULTIPLIER;
            Node<K, V>[] newTable = new Node[newCapacity];
            transfer(newTable);
            table = newTable;
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
