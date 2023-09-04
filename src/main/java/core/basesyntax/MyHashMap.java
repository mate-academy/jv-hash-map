package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndexBucket(key);
        Node<K,V> node = table[index];
        Node<K,V> newNode = new Node<>(key, value);
        if (node == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K,V> currentNode = table[index];
            while (currentNode != null) {
                if (key == currentNode.key || key != null && key.equals((currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexBucket(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
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

    public int getIndexBucket(K key) {
        return key == null ? 0 : hash(key) % DEFAULT_CAPACITY;
    }

    public int hash(K key) {
        return Math.abs(key.hashCode());
    }

    private void resize() {
        int newLength = table.length * 2;
        threshold = (int) (newLength * LOAD_FACTOR);
        Node<K, V>[] oldNode = table;
        table = new Node[newLength];
        size = 0;
        for (Node<K, V> node : oldNode) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
