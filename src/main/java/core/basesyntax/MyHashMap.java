package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key, table.length);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (areKeysEqual(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
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
        int index = getIndex(key, table.length);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (areKeysEqual(currentNode.key, key)) {
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

    private int getIndex(K key, int capacity) {
        return (key == null) ? 0 : (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    private boolean areKeysEqual(K key1, K key2) {
        return (key1 == key2) || (key1 != null && key1.equals(key2));
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);

        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                Node<K, V> nextNode = oldNode.next;
                int newIndex = getIndex(oldNode.key, newCapacity);
                oldNode.next = newTable[newIndex];
                newTable[newIndex] = oldNode;
                oldNode = nextNode;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
