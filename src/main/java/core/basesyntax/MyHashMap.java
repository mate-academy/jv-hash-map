package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (key == null ? current.key == null : key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (key == null ? current.key == null : key.equals(current.key)) {
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
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> headNode : table) {
            while (headNode != null) {
                Node<K, V> next = headNode.next;
                int newIndex = getIndex(headNode.key, newCapacity);

                headNode.next = newTable[newIndex];
                newTable[newIndex] = headNode;

                headNode = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    private int getIndex(K key, int length) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }
    }
}
