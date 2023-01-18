package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final short RESIZE_MULTIPlIER = 2;

    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = getNode(key);
        if (node != null) {
            node.value = value;
            return;
        }

        if (++size > threshold) {
            resize();
        }

        int index = key == null ? 0 : Math.abs(key.hashCode()) % table.length;
        putNodeByIndex(index, new Node<>(key, value));
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K,V> getNode(K key) {
        int arrayIndex = 0;
        if (key != null) {
            arrayIndex = Math.abs(key.hashCode()) % table.length;
        }

        Node<K, V> node = table[arrayIndex];

        while (node != null) {
            if ((node.key == null && key == null)
                    || (
                    (node.key != null && key != null)
                    && (node.key.hashCode() == key.hashCode())
                    && node.key.equals(key)
                )
            ) {
                return node;
            }
            node = node.next;
        }

        return null;
    }

    private void resize() {
        final int newSize = table.length * RESIZE_MULTIPlIER;
        Node<K, V>[] oldTable = table;
        table = new Node[newSize];
        threshold = (int) (table.length * LOAD_FACTOR);

        for (Node<K, V> cell: oldTable) {
            Node<K, V> node = cell;
            while (node != null) {
                Node<K, V> nextNode = node.next;

                int arrayIndex = Math.abs(node.key.hashCode()) % newSize;
                node.next = null;
                putNodeByIndex(arrayIndex, node);
                node = nextNode;
            }
        }
    }

    private void putNodeByIndex(int index, Node<K, V> node) {
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> nodeFromTable = table[index];
            while (nodeFromTable.next != null) {
                nodeFromTable = nodeFromTable.next;
            }
            nodeFromTable.next = node;
        }
    }
}
