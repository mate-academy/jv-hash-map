package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if ((node.key == key || (key != null && key.equals(node.key)))) {
                node.value = value;
                return;
            }
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if ((node.key == key || (key != null && key.equals(node.key)))) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexFor(int hash, int size) {
        return Math.abs(hash % size);
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * CAPACITY_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        transfer(newTable);
        table = newTable;
    }

    private void transfer(Node<K, V>[] newTable) {
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = indexFor(hash(node.key), newTable.length);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
