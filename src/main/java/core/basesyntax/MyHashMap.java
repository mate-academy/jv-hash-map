package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCapacity = 16;
    private final float loadFolder = 0.75f;
    private int size = 0;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[defaultCapacity];
    }

    @Override
    public void put(K key, V value) {
        int length = table.length;
        int checkSize = size;
        if (size >= length * loadFolder) {
            resize();
        }
        Node newNode = table[length - 1 & hash(key)];
        if (newNode == null) {
            table[length - 1 & hash(key)] = new Node(hash(key), key, value, null);
            size++;
        } else {
            while (newNode != null) {
                if (key == newNode.key || (key != null && key.equals(newNode.key))) {
                    newNode.value = value;
                    checkSize++;
                }
                newNode = newNode.next;
            }
            if (checkSize == size) {
                newNode.next = new Node(hash(key), key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[table.length - 1 & hash(key)];
        while (currentNode != null) {
            if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K,V>[] newTable = table;
        size = 0;
        table = new Node[newTable.length * 2];
        Node<K, V> currentNode;
        for (int i = 0; i < newTable.length; i++) {
            if (newTable[i] != null) {
                currentNode = newTable[i];
                put(currentNode.key, currentNode.value);
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() * 31;
    }

    private static class Node<K, V> {
        private Node next;
        private final int hash;
        private V value;
        private final K key;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
