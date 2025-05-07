package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        if (key == null) {
            putWithNullKey(value);
            return;
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(new Node<>(key.hashCode(), key, value, null));
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getValueForNullKey();
        }
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (key.equals(node.key)) {
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
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * CAPACITY_MULTIPLIER];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                node.next = null;
                addNode(node);
                node = next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private void addNode(Node<K, V> newNode) {
        int index = getIndex(newNode.key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> node = table[index];
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
        size++;
    }

    private void putWithNullKey(V value) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (node.key == null) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
        }
        addNode(new Node<>(0, null, value, null));
    }

    private V getValueForNullKey() {
        Node<K, V> node = table[0];
        while (node != null) {
            if (node.key == null) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
