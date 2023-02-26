package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private static final int NULL_INDEX = 0;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > DEFAULT_LOAD_FACTOR * table.length) {
            resize();
        }
        Node<K, V> checkNode = table[hashCode(key)];
        Node<K, V> newNode = new Node<>(hashCode(key), key, value, null);
        if (checkNode == null) {
            size++;
            table[hashCode(key)] = newNode;
            return;
        }
        for (; ; ) {
            if (key == checkNode.key) {
                checkNode.value = value;
                return;
            }
            if (checkNode.key != null && checkNode.key.equals(key)) {
                checkNode.value = value;
                return;
            }
            if (checkNode.next == null) {
                checkNode.next = newNode;
                size++;
                return;
            }
            checkNode = checkNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hashCode(key)];
        while (node != null) {
            if (key == node.key) {
                return node.value;
            }
            if (node.key != null && node.key.equals(key)) {
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
        Node<K, V>[] newTable = new Node[table.length];
        System.arraycopy(table, 0, newTable, 0, table.length);
        table = new Node[table.length * CAPACITY_MULTIPLIER];
        size = 0;
        replaceNodes(newTable);
    }

    private void replaceNodes(Node<K, V>[] newTable) {
        for (Node<K, V> node : newTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hashCode(K key) {
        if (key == null) {
            return NULL_INDEX;
        }
        return key.hashCode() > 0 ? key.hashCode() % table.length
                : -1 * key.hashCode() % table.length;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
