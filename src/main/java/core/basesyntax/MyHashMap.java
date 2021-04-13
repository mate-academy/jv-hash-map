package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

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

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        putNodeInTable(newNode, table);
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null && table.length > 0) {
            int binNumber = getBinNumber(key, table.length);
            Node<K, V> currentBin = table[binNumber];
            while (currentBin != null) {
                if (Objects.equals(key, currentBin.key)) {
                    return currentBin.value;
                }
                currentBin = currentBin.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void putNodeInTable(Node<K, V> node, Node<K, V>[] inputTable) {
        int binNumber = getBinNumber(node.key, inputTable.length);
        if (inputTable[binNumber] == null) {
            inputTable[binNumber] = node;
        } else {
            Node<K, V> currentBin = inputTable[binNumber];
            Node<K, V> prevBin = null;
            while (currentBin != null) {
                if (Objects.equals(node.key, currentBin.key)) {
                    currentBin.value = node.value;
                    return;
                }
                prevBin = currentBin;
                currentBin = currentBin.next;
            }
            if (prevBin != null) {
                prevBin.next = node;
            }
        }
        ++size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold;
        if (oldCapacity > 0) {
            newCapacity = oldCapacity * 2;
            newThreshold = oldThreshold * 2;
        } else {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        threshold = newThreshold;
        table = new Node[newCapacity];
        if (oldTable != null) {
            rearrangeBins(oldTable, newCapacity);
        }
    }

    private int getBinNumber(K key, int tableCapacity) {
        return hash(key) % tableCapacity;
    }

    private void rearrangeBins(Node<K, V>[] oldTable, int newCapacity) {
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> currentNode : oldTable) {
            if (currentNode != null) {
                putNodeInTable(currentNode, newTable);
            }
        }
        table = newTable;
    }
}
