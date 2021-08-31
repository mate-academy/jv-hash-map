package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INSTALL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        this.capacity = INSTALL_CAPACITY;
        this.threshold = (int) (INSTALL_CAPACITY * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private static int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            table = resize();
        }
        putValue(new Node<>(hash(key), key, value), table);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> searchingNode = getNode(key);
        return searchingNode == null ? null : searchingNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key) {
        for (Node<K, V> headNode : table) {
            Node<K, V> searchingNode = headNode;
            if (searchingNode != null) {
                while (searchingNode != null) {
                    if (searchingNode.hash == hash(key)
                            && (searchingNode.key == key
                            || (searchingNode.key != null
                            && searchingNode.key.equals(key)))) {
                        return searchingNode;
                    }
                    searchingNode = searchingNode.next;
                }
            }
        }
        return null;
    }

    private Node<K, V>[] resize() {
        size = 0;
        capacity = capacity << 1;
        threshold = threshold << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> headNode : table) {
            Node<K, V> currentNode = headNode;
            if (currentNode != null) {
                while (currentNode != null) {
                    putValue(cleanNode(currentNode), newTable);
                    currentNode = currentNode.next;
                }
            }
        }
        return newTable;
    }

    private void putValue(Node<K, V> node, Node<K, V>[] table) {
        Node<K, V> prevNode = table[node.hash % table.length];
        if (prevNode == null) {
            table[node.hash % table.length] = node;
            size++;
            return;
        }
        while (true) {
            if (prevNode.hash == node.hash
                    && (prevNode.key == node.key
                    || (prevNode.key != null
                    && prevNode.key.equals(node.key)))) {
                prevNode.value = node.value;
                return;
            }
            if (prevNode.next == null) {
                prevNode.next = node;
                size++;
                return;
            }
            prevNode = prevNode.next;
        }
    }

    private Node<K, V> cleanNode(Node<K, V> node) {
        return new Node<>(node.hash, node.key, node.value);
    }
}
