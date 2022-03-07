package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int size = 0;

    private static class Node<K, V> {
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

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == (int) (table.length * LOAD_FACTOR)) {
            resize();
        }
        if (putValue(hash(key), key, value)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> previousNode = null;
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if ((node.key != null
                    && node.key.equals(key))
                    || node.key == key) {
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

    private void resize() {
        int newTableLength = table.length * 2;
        Node<K,V>[] newTable = new Node[newTableLength];
        Node<K, V>[] tableCopy = table;
        table = newTable;
        for (Node<K, V> node : tableCopy) {
            while (node != null) {
                putValue(hash(node.key), node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean putValue(int hash, K key, V value) {
        Node<K, V> node = new Node<>(hash, key, value, null);
        if (table[hash] == null) {
            table[hash] = node;
        } else {
            Node<K, V> previousNode = null;
            Node<K, V> currentNode = table[hash];
            while (currentNode != null) {
                if ((currentNode.key != null
                        && currentNode.key.equals(node.key))
                        || currentNode.key == key) {
                    if (previousNode == null) {
                        node.next = currentNode.next;
                        table[hash] = node;
                        return false;
                    } else {
                        node.next = currentNode.next;
                        previousNode.next = node;
                        return false;
                    }
                }
                previousNode = currentNode;
                currentNode = currentNode.next;

            }
            previousNode.next = node;
        }
        return true;
    }

    private int hash(K key) {
        if (key != null) {
            int hash = key.hashCode();
            if (hash < 0) {
                hash *= -1;
            }
            hash = hash % table.length;
            return hash;
        }
        return 0;
    }
}
