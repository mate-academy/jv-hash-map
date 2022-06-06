package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node(key, value, null);
            size++;
        } else {
            Node<K, V> sameNode = findNodeInBucket(node, key);
            if (sameNode != null) {
                sameNode.value = value;
            } else {
                Node<K, V> tail = getTail(node);
                tail.next = new Node(key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> sameNode = findNodeInBucket(table[index], key);
        if (sameNode != null) {
            return sameNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K,V> currNode = oldTable[i];
            if (currNode == null) {
                continue;
            }
            while (currNode != null) {
                put(currNode.key, currNode.value);
                currNode = currNode.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    private Node<K, V> findNodeInBucket(Node<K, V> node, K key) {
        while (node != null) {
            if (node.key == key || (key != null && key.equals(node.key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private Node<K, V> getTail(Node<K, V> head) {
        while (head.next != null) {
            head = head.next;
        }
        return head;
    }
}
