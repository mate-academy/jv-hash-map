package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75d;

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;
    }

    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private int size;
    private int index;

    private Node<K, V>[] table = (Node<K, V>[]) new Node[capacity];

    @Override
    public void put(K key, V value) {
        Node<K, V> existedNode = searchHashMap(key);
        if (existedNode == null) {
            newHashMapNode(key, value);
        } else {
            existedNode.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> existedNode;
        return (existedNode = searchHashMap(key)) == null ? null : existedNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() % capacity;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        final int oldCapacity = capacity;
        capacity = capacity << 1;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null) {
                newHashMapNode(oldTable[i].key, oldTable[i].value);
                while (oldTable[i].next != null) {
                    newHashMapNode(oldTable[i].next.key, oldTable[i].next.value);
                    oldTable[i] = oldTable[i].next;
                }
            }
        }
    }

    private Node<K, V> searchHashMap(K key) {
        Node<K, V> tempNode = table[0];
        while (tempNode != null) {
            if (tempNode.key == key || tempNode.key != null && tempNode.key.equals(key)) {
                return tempNode;
            }
            tempNode = tempNode.next;
        }
        if (key == null) {
            return null;
        }
        for (int i = 1; i < capacity; i++) {
            tempNode = table[i];
            while (tempNode != null) {
                if (tempNode.key.equals(key)) {
                    return tempNode;
                }
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    private void newHashMapNode(K key, V value) {
        if (size == threshold) {
            resize();
        }
        index = hash(key);
        if (index < 0) {
            index = index + capacity;
        }
        if (table[index] == null) {
            newBucket(key, value);
        } else {
            newLink(key, value);
        }
    }

    private void newBucket(K key, V value) {
        table[index] = newNode(key, value);
    }

    private void newLink(K key, V value) {
        Node<K, V> temp = table[index];
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode(key, value);
    }

    private Node<K, V> newNode(K key, V value) {
        Node<K, V> newNode = new Node<>();
        newNode.hash = (key == null) ? 0 : key.hashCode();
        newNode.key = key;
        newNode.value = value;
        newNode.next = null;
        size++;
        return newNode;
    }
}
