package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(key, value);
        int index = searchNodePosition(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            putCollisionNode(table[index], node);
        }
    }

    @Override
    public V getValue(K key) {
        if (size <= 0) {
            return null;
        }
        int index = searchNodePosition(key);
        return searchValue(table[index], key);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size >= threshold) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[capacity * 2];
            threshold = threshold * 2;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int searchNodePosition(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putCollisionNode(Node<K, V> existNode, Node<K, V> newNode) {
        if (existNode.key == null && existNode.key == newNode.key) {
            existNode.value = newNode.value;
            return;
        } else if (existNode.key != null && existNode.key.equals(newNode.key)) {
            existNode.value = newNode.value;
            return;
        }

        if (existNode.next == null) {
            existNode.next = newNode;
            size++;
        } else {
            putCollisionNode(existNode.next, newNode);
        }
    }

    private V searchValue(Node<K, V> node, K key) {
        if (key == null && node.key == null) {
            return node.value;
        } else if (node.key != null && node.key.equals(key)) {
            return node.value;
        } else if (node.next != null) {
            return searchValue(node.next, key);
        } else {
            return null;
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
