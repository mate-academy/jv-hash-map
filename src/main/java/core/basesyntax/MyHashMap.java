package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        for (Node<K,V> e = table[index]; e != null; e = e.next) {
            if (e.key == key || e.key != null && e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        if (threshold < size + 1) {
            resize();
        }
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> e = table[index];
            table[index] = new Node<>(key, value, e);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        final Node<K,V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        transfer(oldTable);
    }

    private void transfer(Node[] oldTable) {
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K,V> getNode(K key) {
        for (Node<K,V> e = table[getIndex(key)]; e != null; e = e.next) {
            if (e.key == key || e.key != null && e.key.equals(key)) {
                return e;
            }
        }
        return null;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }
}
