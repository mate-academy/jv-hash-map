package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float RESIZE_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * RESIZE_FACTOR) {
            resize();
        }
        Node newNode = new Node(key, value, null);
        insert(newNode);
    }

    @Override
    public V getValue(K key) {
        int index = getTableIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == currentNode.key || (currentNode.key
                    != null && currentNode.key.equals(key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) ^ (key.hashCode() >>> 16);
    }

    private void insert(Node<K, V> newNode) {
        int index = getTableIndex(newNode.key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        while (true) {
            if (currentNode.key == newNode.key || (currentNode.key
                    != null && currentNode.key.equals(newNode.key))) {
                currentNode.value = newNode.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }

    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getTableIndex(K key) {
        return (key == null) ? 0 : hash(key) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;

        }
    }
}
