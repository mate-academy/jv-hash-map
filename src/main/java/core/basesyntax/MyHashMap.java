package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = indexFor(key);

        Node<K, V> current = table[index];

        while (current != null) {
            if (equalsKey(current.key, key)) {
                current.value = value; // Update existing
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (equalsKey(current.key, key)) {
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

    private int indexFor(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean equalsKey(K k1, K k2) {
        return (k1 == k2) || (k1 != null && k1.equals(k2));
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> head : table) {
            while (head != null) {
                Node<K, V> next = head.next;
                int newIndex = (head.key == null) ? 0 : Math.abs(head.key.hashCode() % newCapacity);
                head.next = newTable[newIndex];
                newTable[newIndex] = head;
                head = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }
}
