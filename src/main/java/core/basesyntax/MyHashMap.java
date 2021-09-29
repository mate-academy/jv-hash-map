package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int INITIAL_SIZE = 0;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] array;

    public MyHashMap() {
        array = new Node[INITIAL_CAPACITY];
        capacity = INITIAL_CAPACITY;
        size = INITIAL_SIZE;
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        Node<K, V> node = new Node(key, value);
        node.hash = getHash(key);
        int index = getIndex(getHash(key));
        addNode(node, index);
    }

    @Override
    public V getValue(K key) {
        return findNodeByKey(key) == null ? null : findNodeByKey(key).value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        if (key != null) {
            return key.hashCode() < 0 ? -key.hashCode() : key.hashCode();
        }
        return 0;
    }

    private int getIndex(int hash) {
        return hash % capacity;
    }

    private void checkCapacity() {
        if (size == capacity * LOAD_FACTOR) {
            Node<K, V>[] newArray = new Node[capacity * 2];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
    }

    private void addNode(Node<K, V> node, int index) {
        if (array[index] == null) {
            array[index] = node;
            size++;
            return;
        }
        Node<K, V> similarKeyNode = findNodeByKey(node.key);
        if (similarKeyNode != null) {
            similarKeyNode.value = node.value;
            return;
        }
        findlastNode(array[index]).next = node;
        size++;
    }

    private Node<K, V> findlastNode(Node<K, V> currentNode) {
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private Node<K, V> findNodeByKey(K key) {
        Node<K, V> currentNode = array[getIndex(getHash(key))];
        while (currentNode != null) {
            if (currentNode.key == null ? key == null : currentNode.key.equals(key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }
}
