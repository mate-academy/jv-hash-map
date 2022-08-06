package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;


    public MyHashMap() {
        table = new Node<>[DEFAULT_CAPACITY];
        size = 0;
    }

    private class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(int hash, Node<K, V> next, K key, V value) {
            this.hash = hash;
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(getHash(key), null, key, value);
        if (table[newNode.hash] == null) {
            table[newNode.hash] = newNode;
        } else {
            setNext(newNode);
        }
        size ++;
        checkSizeOfTable();
    }

    @Override
    public V getValue(K key) {
        if (table[getHash(key)] == null) {
            return null;
        } else {
            Node<K, V> firstNode = table[getHash(key)];
            while (firstNode != null) {
                if (firstNode.key.equals(key)) {
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

    private void checkSizeOfTable() {
        if (size == (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            grow();
        }
    }

    private void grow() {
        Node<K, V>[] newTable = new Node<>[table.length * 2];

    }

    private int getHash(K key) {
        if (key == null) {
            return  0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private void setNext(Node<K, V> node) {
        Node<K, V> prevNode = table[node.hash];
        while (prevNode.next != null) {
            prevNode = prevNode.next;
        }
        prevNode.next = node;
    }
}
