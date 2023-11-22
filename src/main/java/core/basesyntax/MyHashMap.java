package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    static final int DEFAULT_CAPACITY_MULTIPLIER = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.key == null && key == null)
                    || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(key, value, table[index]);
        size++;
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            transfer();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.key == null && key == null)
                    || (node.key != null && node.key.equals(key))) {
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

    private void transfer() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * DEFAULT_CAPACITY_MULTIPLIER];

        for (Node<K, V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
