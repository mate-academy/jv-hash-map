package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int capacity = DEFAULT_CAPACITY;
    private Node[] data = new Node[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        ensureCapacity();
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        Node<K, V> currentNode = data[index];
        Node<K, V> prevNode = null;
        while (currentNode != null) {
            if (((key == currentNode.key)
                    || (key != null && key.equals(currentNode.key)))) {
                currentNode.value = value;
                return;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
        if (prevNode == null) {
            data[index] = newNode;
        } else {
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node testNode = data[index];
        while (testNode != null) {
            if (((key == testNode.key)
                    || (key != null && key.equals(testNode.key)))) {
                return (V) testNode.value;
            }
            testNode = testNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void ensureCapacity() {
        if (size + 1 >= capacity * LOAD_FACTOR) {
            capacity = capacity * CAPACITY_MULTIPLIER;
            size = 0;
            Node[] copyData = data;
            data = new Node[capacity];
            for (Node node : copyData) {
                while (node != null) {
                    put((K) node.key, (V) node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        int index = (key == null) ? 0 : key.hashCode() % capacity;
        return Math.abs(index);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
