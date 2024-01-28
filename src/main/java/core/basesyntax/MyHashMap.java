package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hashCode = (key != null) ? key.hashCode() : 0;
        int insertIndex = Math.abs(hashCode) % table.length;

        Node<K, V> newNode = new Node<>(key, value, null);

        Node<K, V> current = table[insertIndex];
        while (current != null) {
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        newNode.next = table[insertIndex];
        table[insertIndex] = newNode;
        size++;

        if ((double) size / table.length > DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key != null) ? key.hashCode() : 0;
        int index = Math.abs(hash) % table.length;

        Node<K, V> node = table[index];

        while (node != null) {
            if ((node.key == null && key == null) || (node.key != null && node.key.equals(key))) {
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
        Node<K, V>[] newTable = new Node[newCapacity];

        int newSize = 0;

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;

                int newHash = (node.key != null) ? node.key.hashCode() : 0;
                int newIndex = Math.abs(newHash) % newCapacity;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
                newSize++;
            }
        }

        table = newTable;
        size = newSize;
    }

    private static class Node<K, V> {
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
