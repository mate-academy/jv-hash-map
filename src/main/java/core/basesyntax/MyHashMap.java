package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= DEFAULT_LOAD_FACTOR * table.length) {
            resize();
        }
        int indexTable = getHashIndex(key);
        Node<K, V> currentNode = table[indexTable];
        if (currentNode == null) {
            table[indexTable] = new Node<>(key, value, null);
        }
        while (currentNode != null) {
            if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                break;
            }
            currentNode = currentNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getHashIndex(key)];
        while (currentNode != null) {
            if (key == currentNode.key
                    || key != null
                    && key.equals(currentNode.key)) {
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
        private Node<K,V> next;

        Node(K incomingKey, V incomingValue, Node<K, V> incomingNext) {
            key = incomingKey;
            value = incomingValue;
            next = incomingNext;
        }
    }

    private int getHashIndex(K key) {
        return key != null ? Math.abs(key.hashCode()) % table.length : 0;
    }

    private void resize() {
        Node<K, V>[] previousTable = table;
        table = new Node[previousTable.length << 1];
        size = 0;
        for (Node<K, V> newTableElements : previousTable) {
            while (newTableElements != null) {
                put(newTableElements.key, newTableElements.value);
                newTableElements = newTableElements.next;
            }
        }
    }
}
