package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INCREMENT = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int bucketIndex = getIndex(key);
        Node<K, V> node = table[bucketIndex];
        if (node == null) {
            table[bucketIndex] = newNode;
            size++;
            return;
        }

        while (node.next != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
            node = node.next;

        }

        if (node.key == key || (node.key != null && node.key.equals(key))) {
            node.value = value;
            return;
        }
        node.next = newNode;
        size++;
        return;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getIndex(key);
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
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

    private void resize() {
        if (size >= threshold) {
            Node[] oldTable = table;
            int oldCapacity = table.length;
            int newCapacity = oldCapacity * DEFAULT_INCREMENT;
            Node[] newTable = new Node[newCapacity];
            table = newTable;
            size = 0;
            transfer(oldTable);
            threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        }
    }

    private void transfer(Node[] oldTable) {
        for (int j = 0; j < oldTable.length; j++) {
            Node<K, V> transferNode = oldTable[j];
            while (transferNode != null) {
                put(transferNode.key, transferNode.value);
                transferNode = transferNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
