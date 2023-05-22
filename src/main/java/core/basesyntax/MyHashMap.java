package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > (int)(table.length * LOAD_FACTOR)) {
            resize();
        }
        Node<K, V> node = table[getIndex(key)];
        if (node == null) {
            table[getIndex(key)] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
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
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (key == node.key || (node.key != null && node.key.equals(key))) {
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
        int newCapacity = table.length << 1;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
