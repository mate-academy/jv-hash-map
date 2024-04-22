package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;

    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (++size > threshold) {
            resize();
        }
        placeNode(table, key, value, hash(key));
    }

    @Override
    public V getValue(K key) {
        int keyPosition = getNodeIndex(hash(key), table);
        Node<K, V> foundNode = table[keyPosition];
        while (foundNode != null) {
            if (keysAreEqual(foundNode.key, key)) {
                return foundNode.value;
            }
            foundNode = foundNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[table.length * 2];
        threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node : table) {
            if (node == null) {
                continue;
            }
            while (node != null) {
                placeNode(newTable, node.key, node.value, node.keyHash);
                node = node.next;
            }
        }
        table = newTable;
    }

    private void placeNode(Node<K, V>[] table, K key, V value, int keyHash) {
        int nodePosition = getNodeIndex(keyHash, table);

        Node<K,V> newNode = new Node<>(key, value);
        Node<K,V> placedNode = table[nodePosition];
        if (placedNode == null) {
            table[nodePosition] = newNode;
            return;
        }

        while (placedNode != null) {
            if (keysAreEqual(placedNode.key, key)) {
                placedNode.value = value;
                size--;
                return;
            }

            if (placedNode.next == null) {
                placedNode.next = newNode;
                return;
            }

            placedNode = placedNode.next;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getNodeIndex(int keyHash, Node<K, V>[] table) {
        return Math.abs(keyHash % table.length);
    }

    private boolean keysAreEqual(K key1, K key2) {
        return (key1 == key2) || (key1 != null && key1.equals(key2));
    }

    private static class Node<K, V> {
        private final int keyHash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.keyHash = key == null ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
        }
    }
}
