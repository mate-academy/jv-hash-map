package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_SIZE = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        capacity = INITIAL_SIZE;
        table = getTable(capacity);
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = indexForBucket(key,capacity);
        Node<K, V> currentNode = table[bucketIndex];
        if (currentNode == null) {
            return null;
        }
        if (checkEquality(currentNode.key, key)) {
            return table[bucketIndex].value;
        } else {
            while (currentNode != null) {
                if (checkEquality(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int bucketIndex = indexForBucket(key,capacity);
        Node<K,V> currentNode = table[bucketIndex];
        if (currentNode == null) {
            table[bucketIndex] = new Node<>(hash(key), key, value, null);
            size++;
        } else {
            while (currentNode.next != null) {
                if (checkEquality(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (checkEquality(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(hash(key), key, value, null);
                size++;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K,V> [] newTable = getTable(newCapacity);
        threshold = (int) (capacity * LOAD_FACTOR);
        threshold = threshold * 2;
        transfer(table, newTable);
        table = newTable;
        capacity = newCapacity;
    }

    private void transfer(Node<K,V> [] oldTable, Node<K,V> [] newTable) {
        int index = 0;
        while (index < oldTable.length) {
            Node<K,V> currentNode = oldTable[index];
            if (currentNode != null) {
                do {
                    oldTable[index] = null;
                    int bucketIndex = indexForBucket(currentNode.key, newTable.length);
                    Node<K, V> next = currentNode.next;
                    currentNode.next = newTable[bucketIndex];
                    newTable[bucketIndex] = currentNode;
                    currentNode = next;
                } while (currentNode != null);
            }
            index++;
        }
    }

    private int indexForBucket(Object key, int capacity) {
        return key == null ? 0 : key.hashCode() & (capacity - 1);
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private Node<K,V>[] getTable(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }

    private boolean checkEquality(K firstElement, K secondElement) {
        return firstElement == secondElement
                || firstElement != null && firstElement.equals(secondElement);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
