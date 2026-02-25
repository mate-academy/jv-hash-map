package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_MULTIPLIER = 2;
    private int capacity;
    private int size;
    private int threshold;

    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int hash = (key == null) ? 0 : key.hashCode();
        int index = hash & (capacity - 1);

        Node<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.key == null)
                    || (key != null && key.equals(current.key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        int index = hash & (capacity - 1);

        Node<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.key == null)
                    || (key != null && key.equals(current.key))) {
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

    private void resize() {
        capacity *= RESIZE_MULTIPLIER;
        threshold = (int) (capacity * LOAD_FACTOR);

        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];

        for (Node<K, V> head : oldTable) {
            while (head != null) {
                Node<K, V> next = head.next;

                int index = head.hash & (capacity - 1);
                head.next = table[index];
                table[index] = head;

                head = next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
