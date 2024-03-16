package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_FACTOR = 2;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (keysAreEqual(key, currentNode.key)) {
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
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (keysAreEqual(key, currentNode.key)) {
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
        return (key == null) ? 0 : (key.hashCode() % table.length + table.length) % table.length;
    }

    private boolean keysAreEqual(K key1, K key2) {
        return (key1 == null && key2 == null) || (key1 != null && key1.equals(key2));
    }

    private void resize() {
        if (size >= LOAD_FACTOR * table.length) {
            int newCapacity = table.length * RESIZE_FACTOR;
            Node<K, V>[] oldTable = table;
            table = new Node[newCapacity];
            size = 0;
            for (Node<K, V> currentNode : oldTable) {
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
