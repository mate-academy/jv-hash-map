package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] elements;

    public MyHashMap() {
        elements = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= elements.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> currentNode = elements[index];
        while (currentNode != null) {
            if ((key == null && currentNode.key == null)
                    || (key != null && key.equals(currentNode.key))) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value, elements[index]);
        elements[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = elements[index];
        while (currentNode != null) {
            if ((key == null && currentNode.key == null)
                    || (key != null && key.equals(currentNode.key))) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % elements.length);
    }

    private void resize() {
        Node<K, V>[] newElements = (Node<K, V>[]) new Node[elements.length * MULTIPLIER];
        Node<K, V>[] oldElements = elements;
        elements = newElements;
        size = 0;

        for (Node<K, V> headNode : oldElements) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
