package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = (key == null) ? 0 : getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if ((key == null && currentNode.key == null)
                        || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = new Node<>(key, value);
            size++;
        }
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if ((key == null && currentNode.key == null)
                    || (key != null && key.equals(currentNode.key))) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() % table.length + table.length) % table.length;
    }

    private int getIndex(K key, int capacity) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() % capacity + capacity) % capacity;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                int newIndex = getIndex(currentNode.key, newCapacity);
                if (newTable[newIndex] == null) {
                    newTable[newIndex] = new Node<>(currentNode.key, currentNode.value);
                } else {
                    Node<K, V> newNode = newTable[newIndex];
                    while (newNode.next != null) {
                        newNode = newNode.next;
                    }
                    newNode.next = new Node<>(currentNode.key, currentNode.value);
                }
                currentNode = currentNode.next;
            }
        }

        table = newTable;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
