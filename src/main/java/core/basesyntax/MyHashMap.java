package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_CAPACITY = 2;
    private Node<K, V>[] nodes;
    private int size;

    public MyHashMap() {
        nodes = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    static class Node<K, V> {
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
        if (size == trashHold()) {
            resize();
        }
        int index = bucketIndex(key);
        Node<K, V> currentNode = new Node<>(key, value, null);
        if (nodes[index] == null) {
            nodes[index] = currentNode;
            size++;
            return;
        }
        if (key == null) {
            nodes[index] = currentNode;
            return;
        }
        Node<K, V> node = nodes[index];
        while (node.next != null || node.key.equals(key)) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        node.next = currentNode;
        size++;
    }

    private int bucketIndex(K key) {
        return key == null ? 0 : Math.abs(hashCode()) % nodes.length;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = nodes[bucketIndex(key)];
        if (key == null) {
            return currentNode.value;
        }
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
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

    private int trashHold() {
        return (int) (nodes.length * DEFAULT_LOAD_FACTOR);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] copyNodes = nodes;
        nodes = new Node[nodes.length * INCREASE_CAPACITY];
        for (Node<K, V> node : copyNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
