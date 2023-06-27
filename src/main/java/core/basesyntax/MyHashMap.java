package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INCREASE_FACTOR = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Node<K, V>[] table;
    private int defaultCapacityThreshold;

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        defaultCapacityThreshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        double newThreshold = table.length * LOAD_FACTOR;
        double threshold = (table.length == 0) ? defaultCapacityThreshold : newThreshold;
        if (table == null || size >= threshold) {
            resizeAndTransfer(key);
        }
        int index = getIndexByHashCode(key, table.length);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (keysAreEqual(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int index = getIndexByHashCode(key, table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if (keysAreEqual(current.key, key)) {
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

    private int getIndexByHashCode(K key, int tableLength) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        return hashCode & (tableLength - 1);
    }

    private boolean keysAreEqual(K key1, K key2) {
        if (key1 == null) {
            return key2 == null;
        }
        return key1.equals(key2);
    }

    private void resizeAndTransfer(K key) {
        int newLength = table.length * INCREASE_FACTOR;
        Node<K, V>[] newTable = new Node[newLength];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = getIndexByHashCode(current.key, newLength);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
    }
}
