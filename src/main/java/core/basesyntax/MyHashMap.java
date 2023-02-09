package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }

        int hash = hash(key, table.length);
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = new Node<>(key, value);
        } else {
            while (node.next != null) {
                if (key != null && key.equals(node.key)
                        || node.key == key) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (key != null && key.equals(node.key)
                    || node.key == key) {
                node.value = value;
                return;
            }
            node.next = new Node<K, V>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key, table.length);
        Node<K,V> node = table[hash];

        while (node != null) {
            if (key != null && key.equals(node.key)
                    || node.key == key) {
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        capacity = table.length * 2;
        Node<K, V>[] newTable = new Node[capacity];
        threshold = (int) (DEFAULT_LOAD_FACTOR * capacity);
        Node<K, V>[] oldTable = table;
        table = newTable;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            while (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
                oldTable[i] = oldTable[i].next;
            }
        }
    }

    private int hash(K key, int capacity) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }
}
