package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        int threshold = (int) (LOAD_FACTOR * table.length);
        if (size == threshold) {
            resize();
        }
        if (putNodeIntoTable(key, value, table)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int tablePosition = getTablePosition(key,table);
        Node<K, V> node = getNodeFromTableCell(table[tablePosition], key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[table.length << 1];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                putNodeIntoTable(node.key, node.value, newTable);
                node = node.next;
            }
        }
        table = newTable;
    }

    private int getTablePosition(K key, Node<K, V>[] table) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean putNodeIntoTable(K key, V value, Node<K, V>[] table) {
        int tablePosition = getTablePosition(key,table);
        Node<K, V> node = getNodeFromTableCell(table[tablePosition], key);
        if (node != null) {
            node.value = value;
            return false;
        }
        table[tablePosition] = new Node<>(key, value, table[tablePosition]);
        return true;
    }

    private Node<K, V> getNodeFromTableCell(Node<K, V> cellFirstNode, K desiredKey) {
        Node<K, V> node = cellFirstNode;
        K key = desiredKey;
        while (node != null) {
            if ((key == null && node.key == null) || (node.key != null && node.key.equals(key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
