package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K,V>[] table;
    private int threshold;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K,V> newNode = new Node<>(key, value, null);
        Node<K,V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (key != null && key.equals(currentNode.key)
                    || (key == null && currentNode.key == null)) {
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
        table[getIndex(key)] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getIndex(key)];
        while (node != null) {
            if (node.key != null && node.key.equals(key)
                    || node.key == key) {
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

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return key != null ? Math.abs(key.hashCode() % table.length) : 0;
    }

    private void resize() {
        int capacity = table.length * 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
