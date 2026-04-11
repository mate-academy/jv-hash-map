package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        int index = index(key);
        Node<K, V> current = this.table[index];

        while (current != null) {
            if ((key == null && current.key == null)
                    || (key != null && key.equals(current.key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = this.table[index];
        this.table[index] = newNode;
        this.size++;
    }

    @Override
    public V getValue(K key) {
        int index = index(key);
        Node<K, V> current = this.table[index];
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
        return this.size;
    }

    private void resize() {
        Node<K, V>[] oldTable = this.table;

        int oldCapacity = this.capacity;
        this.capacity = oldCapacity * RESIZE_MULTIPLIER;

        this.table = new Node[this.capacity];

        for (Node<K, V> head : oldTable) {
            while (head != null) {
                Node<K, V> next = head.next;

                int index = index(head.key);

                head.next = this.table[index];
                this.table[index] = head;

                head = next;
            }
        }
    }

    private int hash(K value) {
        if (value == null) {
            return 0;
        }
        int h = value.hashCode();
        return h ^ (h >>> 16);
    }

    private int index(K key) {
        return hash(key) & (this.capacity - 1);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
