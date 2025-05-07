package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_ARRAY_EXPANSION = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = getIndex(key);
        Node<K, V> currentNode = table[hash];
        for (int i = 0; i < size; i++) {
            if (currentNode.key == key || currentNode.key != null
                    && currentNode.key.equals(key)) {
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

    private void resize() {
        threshold = (int) (table.length * DEFAULT_ARRAY_EXPANSION * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[DEFAULT_ARRAY_EXPANSION * table.length];
        size = 0;
        moveValue(oldTable);
    }

    private void moveValue(Node<K, V>[] oldTable) {
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                putValue(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void putValue(K key, V value) {
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            for (int i = 0; i < size; i++) {
                if (currentNode.key == key || currentNode.key != null
                        && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
