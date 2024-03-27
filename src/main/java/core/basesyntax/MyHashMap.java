package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.getKey().equals(key) || key == null) {
                e.setValue(value);
                return;
            }
        }
        addNode(key, value, index);
    }

    private void addNode(K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.getValue();
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key);
        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.getKey().equals(key) || key == null) {
                return e;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_MULTIPLIER];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.getNext();
            }
        }
    }

    private int getIndex(K key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        return Math.abs(hashCode) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
