package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o.getClass().equals(Node.class)) {
                Node e = (Node) o;
                return e.key.equals(key) && e.value.equals(value);
            }
            return false;
        }
    }

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }
        int hash = getHash(key);

        Node<K, V> node = new Node<>(hash, key, value, null);

        Node<K, V> nodeByHash = table[hash];
        boolean increaseSize;
        if (nodeByHash == null) {
            table[hash] = node;
            increaseSize = true;
        } else {
            increaseSize = putNextOrChange(nodeByHash, node);
        }

        if (increaseSize && ++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }

        Node<K, V> node = table[getHash(key)];

        if (node == null) {
            return null;
        } else {
            do {
                if (node.key == key
                        || (node.key != null
                        && node.key.equals(key)
                        && node.key.hashCode() == key.hashCode())) {
                    return node.value;
                }

                node = node.next;
            } while (node != null);
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putData(Node<K, V> data) {
        int hash = getHash(data.key);
        data.hash = hash;

        Node<K, V> nodeByHash = table[hash];

        if (nodeByHash == null) {
            table[hash] = data;
        } else {
            putNextOrChange(nodeByHash, data);
        }
    }

    private boolean putNextOrChange(Node<K, V> src, Node<K, V> element) {
        Node<K, V> curr = src;
        Node<K, V> next;
        K elementKey = element.key;

        do {
            next = curr.next;

            if (curr.key == elementKey
                    || (curr.key != null
                    && curr.key.equals(elementKey)
                    && curr.key.hashCode() == elementKey.hashCode())) {
                curr.value = element.value;
                return false;
            }

            if (next == null) {
                curr.next = element;
            }

            curr = next;
        } while (next != null);

        return true;
    }

    private void resize() {
        int oldCapacity = table == null ? 0 : table.length;
        int oldThreshold = threshold;

        if (oldCapacity > 0) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldCapacity << 1];
            threshold = oldThreshold << 1;
            transferAllData(oldTable);
        } else {
            table = new Node[DEFAULT_CAPACITY];
            threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        }

    }

    private void transferAllData(Node<K, V>[] src) {
        Node<K, V> next;
        for (Node<K, V> data : src) {
            while (data != null) {
                next = data.next;
                data.next = null;
                putData(data);
                data = next;
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
