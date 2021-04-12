package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (table == null || table.length == 0) {
            resize();
        }
        int binNumber = getBinNumber(key, table.length);
        Node<K, V> bin = table[binNumber];
        if (bin == null) {
            table[binNumber] = newNode;
        } else {
            for (int count = 0;; ++count) {
                if (bin.hash == hash(key) && (bin.key == key
                        || (key != null && key.equals(bin.key)))) {
                    bin.value = value;
                    return;
                }
                if (bin.next == null) {
                    bin.next = newNode;
                    break;
                }
                bin = bin.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null && table.length > 0) {
            int binNumber = getBinNumber(key, table.length);
            Node<K, V> bin = table[binNumber];
            while (bin != null) {
                if (bin.hash == hash(key) && (bin.key == key
                        || (key != null && key.equals(bin.key)))) {
                    return bin.value;
                }
                bin = bin.next;
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
            table = rearrangeBin(oldTable, newCapacity);
        }
    }

    private int getBinNumber(K key, int tableCapacity) {
        return hash(key) % tableCapacity;
    }

    private Node<K, V>[] rearrangeBin(Node<K, V>[] oldTable, int newCapacity) {
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldTable.length; ++i) {
            Node<K, V> node = oldTable[i];
            if (node != null) {
                oldTable[i] = null;
                do {
                    int binNumber = getBinNumber(node.key, newCapacity);
                    if (newTable[binNumber] == null) {
                        newTable[binNumber] = node;
                    } else {
                        Node<K, V> linkedNode = newTable[binNumber];
                        while (linkedNode.next != null) {
                            linkedNode = linkedNode.next;
                        }
                        linkedNode.next = node;
                    }
                    node = node.next;
                } while (node != null);
            }
        }
        return newTable;
    }
}
