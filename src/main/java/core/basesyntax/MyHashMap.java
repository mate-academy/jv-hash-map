package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((key == null && node.getKey() == null)
                    || (key != null && key.equals(node.getKey()))) {
                node.setValue(value);
                return;
            }
            node = node.getNext();
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.setNext(table[index]);
        table[index] = newNode;
        size++;
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((key == null && node.getKey() == null)
                    || (key != null && key.equals(node.getKey()))) {
                return node.getValue();
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                int index = indexFor(hash(node.getKey()), newCapacity);
                Node<K, V> next = node.getNext();
                node.setNext(newTable[index]);
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int tableLength) {
        return hash & (tableLength - 1);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
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
}
