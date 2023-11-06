package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASE_VALUE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }

        int nodeIndex = getIndex(key);
        Node<K, V> newNode = new Node<>(getKeyHash(key), key, value, null);

        if (table[nodeIndex] == null) {
            table[nodeIndex] = newNode;
        } else {
            Node<K, V> node = table[nodeIndex];
            if (node.next == null) {
                if (isNodeValueSet(node, key, value)) {
                    return;
                }
            } else {
                while (node.next != null) {
                    if (isNodeValueSet(node, key, value)) {
                        return;
                    }
                    node = node.next;
                }
            }
            if (isNodeValueSet(node, key, value)) {
                return;
            }
            node.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }

        int nodeIndex = getIndex(key);

        if (table[nodeIndex] == null) {
            return null;
        } else {
            Node<K, V> current = table[nodeIndex];

            do {
                V value = getNodeValue(current, key);
                if (value != null) {
                    return value;
                }
                current = current.next;
            } while (current.next != null);

            return getNodeValue(current, key);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int keyHash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int keyHash, K key, V value, Node<K, V> next) {
            this.keyHash = keyHash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * INCREASE_VALUE];
        threshold = (int) (newTable.length * LOAD_FACTOR);
        Node<K, V>[] copingTable = table;
        table = newTable;
        size = 0;

        for (Node<K, V> node : copingTable) {
            if (node == null) {
                continue;
            }

            if (node.next == null) {
                put(node.key, node.value);
                continue;
            }

            Node<K, V> current = node;

            do {
                put(current.key, current.value);
                current = current.next;
            } while (current.next != null);

            put(current.key, current.value);
        }
    }

    private int getKeyHash(K key) {
        if (key == null) {
            return 0;
        } else {
            return key.hashCode();
        }
    }

    private int getIndex(K key) {
        return Math.abs(getKeyHash(key) % table.length);
    }

    private boolean isNodeValueSet(Node<K, V> node, K key, V value) {
        if (node.key == null && key == null) {
            node.value = value;
            return true;
        }
        if (node.key != null && node.key.equals(key)) {
            node.value = value;
            return true;
        }
        return false;
    }

    private V getNodeValue(Node<K, V> node, K key) {
        if (node.key == null && key == null) {
            return node.value;
        }
        if (node.key != null && node.key.equals(key)) {
            return node.value;
        }
        return null;
    }
}
