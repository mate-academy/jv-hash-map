package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key == null ? 0 : key.hashCode(), null, key, value);
        if (table[getBucket(key)] == null) {
            if (checkSizeOfTable()) {
                put(key, value);
                return;
            }
            table[getBucket(key)] = newNode;
        } else {
            Node<K, V> currentNode = table[getBucket(key)];
            Node<K, V> lastNode = currentNode;
            while (currentNode != null) {
                if ((currentNode.key == newNode.key)
                        || (currentNode.key != null && currentNode.key.equals(newNode.key))) {
                    currentNode.value = newNode.value;
                    return;
                }
                lastNode = currentNode;
                currentNode = currentNode.next;
            }
            if (checkSizeOfTable()) {
                put(key, value);
                return;
            }
            lastNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table[getBucket(key)] == null) {
            return null;
        } else {
            Node<K, V> firstNode = table[getBucket(key)];
            while (firstNode != null) {
                if (firstNode.key == key || firstNode.key != null && firstNode.key.equals(key)) {
                    return firstNode.value;
                }
                firstNode = firstNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean checkSizeOfTable() {
        if (size == (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            grow();
            return true;
        }
        return false;
    }

    private void grow() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<K, V> temp = oldTable[i];
                while (temp != null) {
                    put(temp.key, temp.value);
                    temp = temp.next;
                }
            }
        }
    }

    private int getBucket(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(int hash, Node<K, V> next, K key, V value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
