package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int COEFFICIENT_FOR_INCREASE = 2;
    private int size;
    private Node<K, V>[] tableOfNodes;

    public MyHashMap() {
        tableOfNodes = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private void increaseSize() {
        size = 0;
        Node<K, V>[] oldBuckets = tableOfNodes;
        tableOfNodes = new Node[tableOfNodes.length * COEFFICIENT_FOR_INCREASE];
        for (Node<K, V> node : oldBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int threshold = (int) (tableOfNodes.length * DEFAULT_LOAD_FACTOR);
        if (threshold == size) {
            increaseSize();
        }

        int index = calculateIndex(key);
        if (tableOfNodes[index] == null) {
            tableOfNodes[index] = newNode;
            size++;
            return;
        }

        Node<K, V> node = tableOfNodes[index];
        while (node != null) {
            if (node.key == key
                    || node.key != null && node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        newNode.next = tableOfNodes[index];
        tableOfNodes[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> node = tableOfNodes[index];
        while (node != null) {
            if (node.key == key
                    || node.key != null && node.key.equals(key)) {
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

    private int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % tableOfNodes.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
