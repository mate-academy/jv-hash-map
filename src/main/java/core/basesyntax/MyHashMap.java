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
        int position = position(node.key);
        if (values[position] == null) {
            values[position] = node;
        } else if (key == values[position].key
                        || key != null && key.equals(values[position].key)) {
            values[position].value = value;
            return;
        } else {
            Node<K, V> current = values[position];
            while (current.next != null) {
                current = current.next;
                if (key == current.key
                        || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
            }
            current.next = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = position(key);
        if (values[position] != null) {
            Node<K, V> node = values[position];
            while (node != null) {
                if (node.key == key
                        || key != null && key.equals(node.key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void insertNode(Node<K, V> node) {
        int position = position(node.key);
        if (values[position] == null) {
            values[position] = node;
        } else {
            Node<K, V> current = values[position];
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node<>(node.key, node.value);
        }
    }

    private int position(K key) {
        return Math.abs(key == null ? 0 : key.hashCode()) % values.length;
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
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
