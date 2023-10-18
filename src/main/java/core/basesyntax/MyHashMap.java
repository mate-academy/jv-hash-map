package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hash = hash(key);
        int index = calculateIndex(hash, table.length);
        Node<K, V> node = findNode(index, key);
        if (node != null) {
            node.value = value;
        } else {
            addEntry(hash, key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int hash = hash(key);
        int index = calculateIndex(hash, table.length);
        Node<K, V> node = findNode(index, key);
        return (node != null) ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        Node<K, V> node = table[0];
        if (node == null) {
            table[0] = new Node<>(0, null, value, null);
            size++;
        } else {
            while (node != null) {
                if (node.key == null) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            addEntry(0, null, value, 0);
        }
    }

    private V getForNullKey() {
        Node<K, V> node = table[0];
        while (node != null) {
            if (node.key == null) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int calculateIndex(int hash, int length) {
        return hash & (length - 1);
    }

    private Node<K, V> findNode(int index, K key) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void addEntry(int hash, K key, V value, int index) {
        Node<K, V> node = table[index];
        table[index] = new Node<>(hash, key, value, node);
        if (++size >= table.length * LOAD_FACTOR) {
            resize(2 * table.length);
        }
    }

    private void transfer(Node<K, V>[] newTable) {
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int j = calculateIndex(node.hash, newTable.length);
                node.next = newTable[j];
                newTable[j] = node;
                node = next;
            }
        }
    }

    private void resize(int newCapacity) {
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
