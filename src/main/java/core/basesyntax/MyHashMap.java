package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int NEW_SIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        } else {
            putWithCollision(newNode);
        }

    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node = table[getIndex(key)]; node != null; node = node.next) {
            if (getKey(node) == key
                    || (getKey(node) != null
                    && getKey(node).equals(key))) {
                return getValueFromNode(node);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public K getKey(Node<K, V> node) {
        return node.key;
    }

    public int getIndex(K key) {
        return key == null ? 0 : key.hashCode() & table.length - 1;
    }

    private void transfer(Node<K, V>[] oldTable) {
        Node<K, V> currentNode;
        for (Node<K, V> transferedNode : oldTable) {
            currentNode = transferedNode;
            while (currentNode != null) {
                put(getKey(currentNode), getValueFromNode(currentNode));
                currentNode = currentNode.next;
            }
        }
    }

    private void resize() {
        int oldCapacity = table.length;
        size = 0;
        Node<K, V>[] oldArray = table;
        table = (Node<K, V>[]) new Node[oldCapacity * NEW_SIZE_MULTIPLIER];
        threshold = (int) (table.length * LOAD_FACTOR);
        transfer(oldArray);
    }

    private void putWithCollision(Node<K, V> node) {
        Node<K, V> currentNode = table[getIndex(node.key)];
        while (currentNode != null) {
            if (getKey(currentNode) == node.key
                    || (getKey(currentNode) != null
                    && getKey(currentNode).equals(node.key))) {
                currentNode.value = node.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private V getValueFromNode(Node<K, V> node) {
        return node.value;
    }

    private boolean isEmpty() {
        return size == 0;
    }
}
