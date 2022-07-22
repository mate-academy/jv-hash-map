package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private Node<K, V> [] table;
    private int threshold;
    private int size;
    private int capacity;

    @Override
    public void put(K key, V value) {
        if (table == null || size == threshold) {
            resize();
        }
        Node<K,V> newNode = new Node<>(getHash(key), key, value);
        int index = newNode.hash % capacity;
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> indexNode = table[index];
            while (indexNode != null) {
                if (equalNodes(indexNode, key)) {
                    indexNode.value = value;
                    return;
                }
                if (indexNode.next == null) {
                    break;
                }
                indexNode = indexNode.next;
            }
            indexNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (capacity == 0) {
            return null;
        }
        int index = getHash(key) % capacity;
        Node<K, V> node = table[index];
        while (node != null) {
            if (equalNodes(node, key)) {
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

    int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        capacity = (table == null) ? DEFAULT_INITIAL_CAPACITY : capacity << 1;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V> [] tableCopy = table;
        table = new Node[capacity];
        if (tableCopy == null) {
            return;
        }
        size = 0;
        for (Node<K, V> node : tableCopy) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    boolean equalNodes(Node<K, V> node, K key) {
        return (node.key == null & key == null)
                || ((node.key != null & key != null)
                && (node.key.hashCode() == key.hashCode() & key.equals(node.key)));
    }

    public class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}

