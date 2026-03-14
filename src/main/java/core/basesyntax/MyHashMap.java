package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private int size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (isKeysEqual(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[getIndex(key)]);
        table[getIndex(key)] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (isKeysEqual(currentNode.key, key)) {
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

    public int getIndex(K key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int index = Math.abs(hashCode) % table.length;
        return index;
    }

    private boolean isKeysEqual(K key1, K key2) {
        return (key1 == key2) || (key1 != null && key1.equals(key2));
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> headNode : oldTable) {
            Node<K, V> currentNode = headNode;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
