package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int COEFFICIENT_DOUBLING_CAPACITY = 2;
    private int size;
    private int fullnessArray;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        fullnessArray = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == fullnessArray) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndexNode(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = newNode;
            size++;
        } else {
            while (currentNode != null) {
                if (getEqualsKeyCheck(key, currentNode.key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndexNode(key)];
        while (currentNode != null) {
            if (getEqualsKeyCheck(key, currentNode.key)) {
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

    private boolean getEqualsKeyCheck(K key, K inputKey) {
        return (key == inputKey) || (inputKey != null && inputKey.equals(key));
    }

    private int getHash(K key) {
        return (key.hashCode() >>> DEFAULT_INITIAL_CAPACITY);
    }

    private int getIndexNode(K key) {
        return key == null ? 0 : getHash(key) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tmpNode = table;
        int newCapacity = tmpNode.length * COEFFICIENT_DOUBLING_CAPACITY;
        table = new Node[newCapacity];
        for (Node<K, V> node : tmpNode) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        fullnessArray = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
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
