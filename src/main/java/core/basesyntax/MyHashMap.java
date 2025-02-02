package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        int index = (table.length - 1) & hash(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.hash == hash(key) && (currentNode.key == key
                    || (key != null && key.equals(currentNode.key)))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void putValue(int hash, K key, V value) {
        if (table == null || table.length == 0) {
            table = resize();
        }
        int bucketIndex = (table.length - 1) & hash(key);
        Node<K, V> currentNode = table[bucketIndex];
        if (currentNode == null) {
            table[bucketIndex] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> tailNode = null;
            while (currentNode != null) {
                if (currentNode.hash == hash && (currentNode.key == key
                        || key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                tailNode = currentNode;
                currentNode = currentNode.next;
            }
            tailNode.next = new Node<>(hash, key, value, null);
        }
        if (++size > threshold) {
            table = resize();
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity = 0;
        int newThreshold = 0;
        if (oldCapacity == 0) {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (oldCapacity > 0) {
            newCapacity = oldCapacity << 1;
            if (newCapacity < MAX_CAPACITY) {
                newThreshold = oldThreshold << 1;
            } else {
                newCapacity = MAX_CAPACITY;
            }
        }
        threshold = newThreshold;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        if (oldTable != null) {
            nodeTransfer(oldTable, newTable);
        }
        table = newTable;

        return table;
    }

    private void nodeTransfer(Node<K, V>[] oldTable, Node<K, V>[] newTable) {
        for (Node<K, V> oldNode : oldTable) {
            if (oldNode != null) {
                Node<K, V> currentNode = oldNode;
                while (currentNode != null) {
                    Node<K, V> nextNode = currentNode.next;
                    currentNode.next = null;
                    int newIndex = currentNode.hash & (newTable.length - 1);
                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = currentNode;
                    } else {
                        Node<K, V> tailNode = newTable[newIndex];
                        while (tailNode.next != null) {
                            tailNode = tailNode.next;
                        }
                        tailNode.next = currentNode;
                    }
                    currentNode = nextNode;
                }
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
