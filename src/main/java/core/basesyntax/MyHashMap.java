package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float RESIZE_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (getSize() == (int) (table.length * RESIZE_FACTOR)) {
            resize();
        }
        Node newNode = new Node(hash(key), key, value, null);
        insert(newNode);
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % table.length;
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
        newNode.next = null;
        int index = newNode.hash % table.length;
        if (index < 0) {
            index += table.length;
        }
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == null && newNode.key == null) {
                    currentNode.value = newNode.value;
                    return;
                }
                if (currentNode.key != null && currentNode.key.equals(newNode.key)) {
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
    }

    private void resize() {
        Node<K, V>[] oldTb = table;
        table = (Node<K, V>[]) new Node[oldTb.length * 2];
        size = 0;
        for (Node<K, V> node : oldTb) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;

        }
    }
}
