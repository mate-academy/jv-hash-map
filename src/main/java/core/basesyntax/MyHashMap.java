package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int ARRAY_LENGTH_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getHash(key, table.length);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(index, key, value, null);
        } else {
            Node<K, V> prevNode = node;
            while (node != null) {
                if ((node.key == key) || (node.key != null && node.key.equals(key))) {
                    node.value = value;
                    return;
                }
                prevNode = node;
                node = node.next;
            }
            prevNode.next = new Node<>(index, key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getHash(key, table.length)];
        while (node != null) {
            if ((node.key == key) || (node.key != null && node.key.equals(key))) {
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
        if (size >= threshold) {
            size = 0;
            Node<K, V>[] tempTable = table;
            table = new Node[table.length * ARRAY_LENGTH_MULTIPLIER];
            threshold = (int) (table.length * LOAD_FACTOR);
            transfer(tempTable);
        }
    }

    private void transfer(Node<K, V>[] tempTable) {
        for (Node<K, V> node : tempTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }

        }
    }

    private int getHash(K key, int length) {
        int index = key == null ? 0 : key.hashCode() % length;
        return index < 0 ? index * -1 : index;
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
