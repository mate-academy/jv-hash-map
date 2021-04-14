package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        table = (Node<K, V>[]) new Node[capacity];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = key == null ? 0 : getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == key
                    || current.key != null && current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        if (size++ > threshold) {
            reSize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            newNode.next = table[index].next;
            table[index].next = newNode;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> neededNode = table[key == null ? 0 : getIndex(key)];
        while (neededNode != null) {
            if (neededNode.key == key
                    || neededNode.key != null && neededNode.key.equals(key)) {
                return neededNode.value;
            }
            neededNode = neededNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void reSize() {
        capacity *= RESIZE_MULTIPLIER;
        threshold *= RESIZE_MULTIPLIER;
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : oldTable) {
            Node<K, V> temp = node;
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
            }
        }
        size++;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
