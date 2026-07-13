package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = (key == null) ? 0 : key.hashCode();
        int index = (table.length - 1) & hash;

        Node<K, V> head = table[index];
        for (Node<K, V> node = head; node != null; node = node.next) {
            if (node.hash == hash && (key == node.key || (key != null && key.equals(node.key)))) {
                node.value = value;
                return;
            }
        }
        Node<K, V> newNode = new Node<>(hash, key, value, head);
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        int index = (table.length - 1) & hash;

        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash && (key == node.key || (key != null && key.equals(node.key)))) {
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
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);

        for (Node<K, V> head : table) {
            while (head != null) {
                Node<K, V> next = head.next;
                int index = (newCapacity - 1) & head.hash;
                head.next = newTable[index];
                newTable[index] = head;
                head = next;
            }
        }
        table = newTable;
    }
}
