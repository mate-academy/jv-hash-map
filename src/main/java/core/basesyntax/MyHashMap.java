package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[hash(key)] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
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

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int oldCapacity = table.length;
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[oldCapacity * DEFAULT_MULTIPLIER];
        for (Node<K, V> old : oldTable) {
            while (old != null) {
                put(old.key, old.value);
                old = old.next;
            }
        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }
}
