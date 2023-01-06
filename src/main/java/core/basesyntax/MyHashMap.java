package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = key == null ? 0 : key.hashCode();
        }
    }


    @Override
    public void put(K key, V value) {
        checkTable();
        insertValue(key, value, table, false);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }

        int index = calculateIndex(key, table);

        for (Node<K,V> node = table[index]; node != null; node = node.next) {
            if (node.key != null && node.key.equals(key) || node.key == key) {
                return node.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkTable() {
        // check if the basket is null
        if (table == null) {
            // create new array of nodes with default capacity
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            // calculate initial threshold
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        // check if the basket has more than 25% of free space available
        if (size + 1 > threshold) {
            // resize the basket if there is less than 25% of free space available
            resize();
        }
    }

    private void resize() {
        int currentCapacity = table.length;
        // calculate new capacity
        int newCapacity = currentCapacity << 1;
        // copy current data into a separate variable
        Node<K, V>[] oldTable = table;
        // create a new basket
        Node<K, V>[] newTable = new Node[newCapacity];
        // move old data to the new basket
        transform(oldTable, newTable);
        // calculate new threshold
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
    }

    private void transform(Node<K, V>[] fromTable, Node<K, V>[] toTable) {
        for (Node<K, V> tableNode : fromTable) {
            if (tableNode == null) {
                continue;
            }

            do {
                insertValue(tableNode.key, tableNode.value, toTable, true);
                tableNode = tableNode.next;
            } while (tableNode != null);
        }

        table = toTable;
    }

    private int calculateIndex(K key, Node<K, V>[] basket) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        return (hash < 0) ? -hash % basket.length : hash % basket.length;
    }

    private void insertValue(K key, V value, Node<K, V>[] toTable, boolean transformation) {
        int index = calculateIndex(key, toTable);
        Node<K, V> newNode = new Node<>(key, value);

        if (toTable[index] == null) {
            toTable[index] = newNode;
            if (!transformation) {
                size++;
            }
            return;
        }

        for (Node<K,V> node = toTable[index]; node != null; node = node.next) {
            if (node.key != null && node.key.equals(newNode.key) || node.key == newNode.key) {
                node.value = value;
                return;
            }

            if (node.next == null) {
                node.next = newNode;
                if (!transformation) {
                    size++;
                }
            }
        }
    }
}
