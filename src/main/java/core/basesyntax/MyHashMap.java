package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double MAX_CAPACITY = 0.75;
    private static final int INDEX_OF_CAPACITY = 2;
    private int size;
    private Node<K, V>[] elements;

    {
        elements = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeed();
        int currentCapacity = elements.length;

        int hash = (key == null) ? 0 : Math.abs(key.hashCode() % elements.length);

        Node<K, V> currentNode = elements[hash];

        while (currentNode != null) {
            if (currentNode.key == key || (key != null && key.equals(currentNode.key))) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value, elements[hash]);
        elements[hash] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> currentNode : elements) {
            while (currentNode != null) {
                if (currentNode.key == key || (key != null && key.equals(currentNode.key))) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeed() {
        int currentCapacity = elements.length;
        int currentSize = getSize();
        if (currentSize > currentCapacity * MAX_CAPACITY) {
            int newCapacity = currentCapacity * INDEX_OF_CAPACITY;
            Node<K, V>[] newElements = new Node[newCapacity];

            for (Node<K, V> currentNode : elements) {
                while (currentNode != null) {
                    K key = currentNode.key;
                    V value = currentNode.value;
                    int newHash = Math.abs(key.hashCode() % newCapacity);

                    Node<K, V> newNode = new Node<>(key, value, null);
                    if (newElements[newHash] != null) {
                        newNode.next = newElements[newHash];
                    }
                    newElements[newHash] = newNode;
                    currentNode = currentNode.next;

                }
            }
            elements = newElements;
        }
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
