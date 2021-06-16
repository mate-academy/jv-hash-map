package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> tableElements = table[transferKeyHashCodeToTableIndex(key)];
        if (tableElements == null) {
            table[transferKeyHashCodeToTableIndex(key)] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (tableElements != null) {
            if (key == tableElements.key || key != null && key.equals(tableElements.key)) {
                tableElements.value = value;
                return;
            }
            if (tableElements.next == null) {
                tableElements.next = new Node<>(key, value, null);
                size++;
                return;
            }
            tableElements = tableElements.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tableElementsByIndex = table[transferKeyHashCodeToTableIndex(key)];
        while (tableElementsByIndex != null) {
            if (key == tableElementsByIndex.key
                    || key != null
                    && key.equals(tableElementsByIndex.key)) {
                return tableElementsByIndex.value;
            }
            tableElementsByIndex = tableElementsByIndex.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K incomingKey, V incomingValue, Node<K, V> incomingNext) {
            key = incomingKey;
            value = incomingValue;
            next = incomingNext;
        }
    }

    private int transferKeyHashCodeToTableIndex(K key) {
        return key != null ? Math.abs(key.hashCode()) % table.length : 0;
    }

    private void resize() {
        Node<K, V>[] previousTable = table;
        threshold = threshold << 1;
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
