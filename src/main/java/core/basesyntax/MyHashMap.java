package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double THRESHOLD_FACTOR = 0.75;
    private static final int CAPACITY_INCREASING_FACTOR = 2;
    private static final int NULL_KEY_INDEX = 0;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == (int) (table.length * THRESHOLD_FACTOR)) {
            resize();
        }
        Node<K, V> node = new Node(key, value);
        int index = calculateIndex(node.key);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        if (table[index].key == node.key || table[index].key != null
                && table[index].key.equals(node.key)) {
            node.next = table[index].next;
            table[index] = node;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null) {
                if (currentNode.next.key == node.key
                        || currentNode.next.key != null && currentNode.next.key.equals(node.key)) {
                    node.next = currentNode.next.next;
                    currentNode.next = node;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = node;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (table[calculateIndex(key)] == null) {
            return null;
        }
        Node<K, V> currentNode = table[calculateIndex(key)];
        while (currentNode != null) {
            if (currentNode.key == key
                    || currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateIndex(K key) {
        return key == null ? NULL_KEY_INDEX : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * CAPACITY_INCREASING_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
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
