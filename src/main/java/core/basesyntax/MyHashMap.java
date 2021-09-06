package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private float threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> savedNode = table[index];
        while (savedNode != null) {
            if (equalsKey(savedNode.key, key)) {
                savedNode.value = value;
                return;
            }
            if (savedNode.next == null) {
                savedNode.next = new Node<>(key, value, null);;
                size++;
                return;
            }
            savedNode = savedNode.next;
        }
        table[index] = new Node<>(key, value, null);;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> pair = table[index];
        while (pair != null) {
            if (equalsKey(pair.key, key)) {
                return pair.value;
            }
            pair = pair.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshold) {
            Node<K, V>[] previousTable = table;
            table = (Node<K, V>[]) new Node[table.length * RESIZE_COEFFICIENT];
            size = 0;
            for (Node<K, V> currentNode : previousTable) {
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return getHashCode(key) % table.length;
    }

    private boolean equalsKey(K nodeKey, K key) {
        return (key == nodeKey) || (key != null && key.equals(nodeKey));
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
