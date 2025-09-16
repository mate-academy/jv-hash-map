package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.size = 0;
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = getIndex(key, table.length);
        Node<K, V> newNode = new Node<>(key, value);

        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }

        Node<K, V> currentNode = table[index];
        while (true) {
            if ((currentNode.key == null && key == null)
                    || (currentNode.key != null && currentNode.key.equals(key))) {
                currentNode.value = value;
                return;
            }

            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }

            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }

        int index = getIndex(key, table.length);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if ((currentNode.key == null && key == null)
                    || (currentNode.key != null && currentNode.key.equals(key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * RESIZE_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> nextNode = node.next;
                int newIndex = getIndex(node.key, newCapacity);

                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = nextNode;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    private int getIndex(K key, int capacity) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() & 0x7fffffff) % capacity;
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
