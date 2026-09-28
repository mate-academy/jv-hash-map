package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private static final int MAX_HASH = 0x7fffffff;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);

        Node<K, V> current = table[index];

        while (current != null) {
            if (isEquals(current.key, key)) {
                current.value = value;
                return;
            }

            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;

        size++;

        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);

        Node<K, V> current = table[index];

        while (current != null) {
            if (isEquals(current.key, key)) {
                return current.value;
            }

            current = current.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return (hash & MAX_HASH) % table.length;
    }

    private boolean isEquals(K key1, K key2) {
        if (key1 == key2) {
            return true;
        }

        if (key1 == null || key2 == null) {
            return false;
        }

        return key1.equals(key2);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;

        table = (Node<K, V>[]) new Node[oldTable.length * RESIZE_FACTOR];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;

                int index = getIndex(node.key);

                node.next = table[index];
                table[index] = node;

                node = next;
            }
        }
    }

    private static class Node<K, V> {

        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
