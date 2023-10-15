package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final Integer KEY_NULL_VALUE = 0;
    private static final Integer BIT_SHIFT_BY_ONE = 1;
    private boolean putedInTable;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        growIfSizeIsInLoadFactory();
        putNodeToArr(table, key, value);
        if (putedInTable) {
            putedInTable = false;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        key = checkIfNull(key);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node current = table[i];
                while (current != null) {
                    if (current.key.equals(key)) {
                        return (V) current.value;
                    }
                    current = current.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNodeToArr(Node[] nodes, K key, V value) {
        key = checkIfNull(key);
        int index = Math.abs(key.hashCode()) % nodes.length;
        Node newNode = new Node<>(key, value, null);
        Node currentNode = nodes[index];
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        newNode.next = nodes[index];
        nodes[index] = newNode;
        putedInTable = true;
    }

    private void growIfSizeIsInLoadFactory() {
        if (size >= table.length * LOAD_FACTOR) {
            Node[] temp = new Node[table.length << BIT_SHIFT_BY_ONE];
            for (int i = 0; i < table.length; i++) {
                Node currentNode = table[i];
                while (currentNode != null) {
                    putNodeToArr(temp, (K) currentNode.key, (V) currentNode.value);
                    currentNode = currentNode.next;
                }
            }
            putedInTable = false;
            table = temp;
        }
    }

    private K checkIfNull(K key) {
        if (key == null) {
            return (K) KEY_NULL_VALUE;
        }
        return key;
    }

    private class Node<K, V> {
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
