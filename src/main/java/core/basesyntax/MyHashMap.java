package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = index(key, table.length);
        Node<K, V> node = getNode(key, index);
        if (node != null) {
            node.value = value;
        } else {
            addNode(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        int index = index(key, table.length);
        Node<K, V> node = getNode(key, index);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int index(K key, int length) {
        return Math.abs(hash(key) % length);
    }

    private Node<K, V> getNode(K key, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (keyEquals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size >= DEFAULT_LOAD_FACTOR * table.length) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean keyEquals(K key1, K key2) {
        return (key1 == key2) || (key1 != null && key1.equals(key2));
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
