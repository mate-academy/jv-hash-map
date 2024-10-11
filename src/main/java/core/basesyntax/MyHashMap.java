package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_CAPACITY;
    private Node<K, V> []table = new Node[capacity];
    private int size = 0;
    private int threhold = 0;

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        node.hash = node.hashCode();
        if (size++ > threhold) {
            resize();
        } else {
            for (int i = 0; i < table.length; i++) {
                if (node.value == null) {
                    table[0] = node;
                    size++;
                }
                if (table[i].hash == node.hash) {
                    size++;
                    table[i].next = node;
                } else {
                    table[node.hash] = node;
                    node.next = table[node.hash + 1];
                    size++;
                }
            }
        }

    }

    @Override
    public V getValue(K key) {
        V value = null;
        for (int i = 0; i < table.length; i++) {
            if (table[i].key == key || key != null && key.equals(table[i].key)) {
                value = table[i].value;
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return 0;
    }

    public void resize() {
        Node<K, V> []table = new Node[capacity];
        capacity = capacity * 2;
        Node<K, V> []newTable = new Node[capacity];
        for (int i = 0; i < table.length; i++) {
            table[i].hash = table[i].hashCode();
            newTable[table[i].hash] = table[i];
            newTable[table[i].hash].next = newTable[table[i].hash + 1];
        }
        System.arraycopy(newTable, 0, table, 0, newTable.length);
    }

    /*
    @Override
    public boolean equals(Node<K, V> node) {
        if (this == node) {
            return true;
        }
        if (node == null) {
            return false;
        }
        return this.getKey() == node.getKey() || this.getKey != null
        && this.getKey().equals(node.getKey());
    }
     */

    @Override
    public int hashCode() {
        int result = 17;
        result = (result * 31) % capacity;
        return result;
    }

    class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public int getHash() {
            return hash;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
