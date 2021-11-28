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

    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private int size;

    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];

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

    private int getIndex(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[newCapacity];
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
        int hash = getIndex(key);
        Node<K, V> tempNode;
        if ((hash == 0) || (key == null)) {
            tempNode = table[0];
        } else {
            tempNode = table[hash];
        }
        while (tempNode != null) {
            if (tempNode.key == key || tempNode.key != null && tempNode.key.equals(key)) {
                return tempNode;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    private void newHashMapNode(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> temp = table[index];
        if (table[index] == null) {
            table[index] = newNode(key, value);
        } else {
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode(key, value);
        }
        size++;
    }

    private Node<K, V> newNode(K key, V value) {
        Node<K, V> newNode = new Node<>();
        newNode.hash = (key == null) ? 0 : key.hashCode();
        newNode.key = key;
        newNode.value = value;
        newNode.next = null;
        return newNode;
    }
}
