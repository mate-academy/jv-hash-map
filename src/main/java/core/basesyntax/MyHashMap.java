package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private static final int INCREASE_RATE = 2;
    private int size;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            if (key == null) {
                this.hash = 0;
            } else {
                this.hash = key.hashCode();
            }
        }
    }

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        addNode(node);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getBucket(key)];
        while (currentNode != null) {
            if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            } else {
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] temporaryTable = table;
        table = new Node[table.length * INCREASE_RATE];
        size = 0;
        for (Node<K, V> nodes : temporaryTable) {
            while (nodes != null) {
                put(nodes.key, nodes.value);
                nodes = nodes.next;
            }
        }
    }

    private void addNode(Node<K, V> node) {
        int h = getBucket(node.key);
        Node<K, V> currentNode = table[h];
        while (currentNode != null) {
            if (currentNode.key == node.key || (currentNode.key != null
                    && currentNode.key.equals(node.key))) {
                currentNode.value = node.value;
                return;
            } else if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[h] = node;
        size++;
    }
}
