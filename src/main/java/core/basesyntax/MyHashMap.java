package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final Double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] innerArray;

    public MyHashMap() {
        innerArray = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkActualSize();
        int index = hash(key);
        Node<K, V> currentNode = innerArray[index];
        while (currentNode != null) {
            if (equalsKeys(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        if (innerArray[index] == null) {
            innerArray[index] = new Node<>(key, value);
        } else {
            currentNode = innerArray[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexOfInputKey = hash(key);
        Node<K, V> foundNode = innerArray[indexOfInputKey];
        while (foundNode != null) {
            if (equalsKeys(foundNode.key, key)) {
                return foundNode.value;
            }
            foundNode = foundNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % innerArray.length;
    }

    private boolean equalsKeys(K k1, K k2) {
        return (k1 == k2 || k1 != null && k1.equals(k2));
    }

    private void checkActualSize() {
        int threshold = (int) (innerArray.length * LOAD_FACTOR);
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldBuckets = innerArray;
        innerArray = new Node[oldBuckets.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> headNode : oldBuckets) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
