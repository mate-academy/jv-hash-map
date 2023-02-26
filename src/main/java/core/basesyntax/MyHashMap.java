package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] values;
    private int size;

    public MyHashMap() {
        values = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        ensureCapacity();
        Node<K, V> node = new Node<>(key, value);
        int position = position(node);
        if (values[position] == null) {
            values[position] = node;
            size++;
        } else {
            if (values[position].put(node)) {
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int position = position(key);
        if (values[position] == null) {
            return null;
        } else {
            return values[position].get(key);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void insertNode(Node<K, V> node) {
        int position = position(node);
        if (values[position] == null) {
            values[position] = node;
        } else {
            values[position].put(node);
        }
    }

    private int position(K key) {
        return Math.abs(key == null ? 0 : key.hashCode()) % values.length;
    }

    private int position(Node<K, V> node) {
        return node.hash % values.length;
    }

    private void ensureCapacity() {
        if (size >= values.length * LOAD_FACTOR) {
            Node<K, V>[] tempValues = values;
            values = (Node<K, V>[]) new Node[values.length * RESIZE_MULTIPLIER];
            for (Node<K, V> bucket : tempValues) {
                Node<K, V> node = bucket;
                while (node != null) {
                    insertNode(new Node<>(node.key, node.value));
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            hash = Math.abs((key == null ? 0 : key.hashCode()));
            this.value = value;
        }

        private boolean put(Node<K, V> node) {
            if (key == node.key || key != null && key.equals(node.key)) {
                value = node.value;
                return false;
            } else if (next != null) {
                return next.put(node);
            } else {
                next = node;
                return true;
            }
        }

        private V get(K key) {
            if (key == this.key || key != null && key.equals(this.key)) {
                return value;
            } else if (next != null) {
                return next.get(key);
            } else {
                return null;
            }
        }
    }
}
