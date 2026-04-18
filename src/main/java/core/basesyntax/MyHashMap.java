package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int size;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key, int length) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % length);
    }

    private boolean keysAreEqual(K key1, K key2) {
        if (key1 == key2) {
            return true;
        }
        return key1 != null && key1.equals(key2);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];
        size = 0;

        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                int newIndex = getIndex(currentNode.key, newCapacity);
                table[newIndex] = new Node<>(currentNode.key, currentNode.value, table[newIndex]);
                size++;
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key, table.length);

        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (keysAreEqual(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        table[index] = new Node<>(key, value, table[index]);
        size++;
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (keysAreEqual(currentNode.key, key)) {
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
}
