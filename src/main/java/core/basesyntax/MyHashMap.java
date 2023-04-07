package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<K, V>(key, value);

        V valueByNewKey = getValue(key);

        if (valueByNewKey != null) {
            if (Objects.equals(value, valueByNewKey)) {
                return;
            }

            putIntoTable(node, table);
            return;
        }

        if (table == null || size == threshold) {
            resize();
        }

        putIntoTable(node, table);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table == null || size == 0) {
            return null;
        }

        int index = key == null ? 0 : Math.abs(key.hashCode()) % table.length;

        Node<K, V> current = table[index];

        if (current == null) {
            return null;
        }

        while (current != null) {
            boolean areKeysEqual = Objects.equals(current.key, key);

            if (areKeysEqual) {
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
        if (table == null) {
            Node<K, V>[] newTable = new Node[DEFAULT_INITIAL_CAPACITY];
            for (int i = 0; i < newTable.length; i++) {
                newTable[i] = null;
            }
            table = newTable;
            threshold = (int)(DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
        } else {
            int newCapacity = table.length * 2;
            threshold = threshold * 2;

            Node<K, V>[] newTable = new Node[newCapacity];

            for (int i = 0; i < newTable.length; i++) {
                newTable[i] = null;
            }

            Node<K, V>[] nodesArray = new Node[size];
            int index = 0;

            for (Node<K, V> element: table) {
                Node<K, V> node = element;

                while (node != null) {
                    nodesArray[index] = node;
                    index++;
                    node = node.next;
                }
            }

            for (Node<K, V> nodeFromArray: nodesArray) {
                nodeFromArray.next = null;
                putIntoTable(nodeFromArray, newTable);
            }

            table = newTable;
        }
    }

    private void putIntoTable(Node<K, V> node, Node<K, V>[] useTable) {
        int index = node.hash % useTable.length;

        Node<K, V> nodeByIndex = useTable[index];

        if (nodeByIndex == null) {
            useTable[index] = node;

            return;
        }

        Node<K, V> current = nodeByIndex;
        Node<K, V> prev = null;

        while (current != null) {
            boolean areKeysEqual = Objects.equals(current.key, node.key);

            if (areKeysEqual) {
                node.next = current.next;

                if (prev != null) {
                    prev.next = node;
                } else {
                    useTable[index] = node;
                }

                return;
            } else {
                if (current.next == null) {
                    current.next = node;

                    return;
                }
            }

            prev = current;
            current = current.next;
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = key == null ? 0 : Math.abs(key.hashCode());
            this.key = key;
            this.value = value;
        }
    }
}
