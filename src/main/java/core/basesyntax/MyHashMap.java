package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int INCREASE_SIZE_OF_ARR_TO = 2;
    private int capacity;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size > capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = hash(key, capacity);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = newNode;
            size++;
        } else {
            while (node.next != null || checkKey(key, node)) {
                if (checkKey(key, node)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            node.next = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key, capacity);
        Node<K, V> node = table[index];
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (checkKey(key, node)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> transfer(Node<K, V> node, Node<K, V>[] newTable) {
        Node<K, V> tempNode = null;
        if (node.next != null) {
            tempNode = node.next;
        }
        node.next = null;
        int index = hash(node.key, capacity);
        if (newTable[index] == null) {
            newTable[index] = node;
        } else {
            Node<K, V> nextNode = newTable[index].next;
            Node<K, V> lastNode = newTable[index];
            while (nextNode != null) {
                if (nextNode.next == null) {
                    lastNode = nextNode;
                    break;
                }
                nextNode = nextNode.next;
            }
            lastNode.next = node;
        }
        return tempNode;
    }

    private void resize() {
        int newCapacity = capacity * INCREASE_SIZE_OF_ARR_TO;
        capacity = newCapacity;
        Node<K, V>[] newTable = new Node[newCapacity];
        Node<K, V> nextNode = null;
        for (Node<K, V> node : table) {
            if (node == null) {
                continue;
            }
            nextNode = transfer(node, newTable);

            while (nextNode != null) {
                nextNode = transfer(nextNode, newTable);
            }
        }
        table = newTable;
    }

    private boolean checkKey(K key, Node<K, V> node) {
        if (node.key == key || node.key != null && node.key.equals(key)) {
            return true;
        }
        return false;
    }

    private int hash(Object key, int capacity) {
        return Math.abs((key == null) ? 0 : key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
