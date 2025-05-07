package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] values;

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        if (values[index] == null) {
            values[index] = new Node(key, value, null);
            size++;
            return;
        }
        Node<K, V> node = values[index];
        while (node != null) {
            if (isEqual(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = getIndex(key);
        Node<K, V> node = values[index];
        while (node != null) {
            if (isEqual(node.key, key)) {
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

    private void resize() {
        if (values == null) {
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            values = new Node[DEFAULT_INITIAL_CAPACITY];
        }
        if (size > threshold) {
            threshold = (int) (values.length * 2 * DEFAULT_LOAD_FACTOR);
            moveToNewCapacity();
        }
    }

    private void moveToNewCapacity() {
        Node<K, V>[] oldValues = values;
        values = new Node[values.length * 2];
        size = 0;
        for (int i = 0; i < oldValues.length; i++) {
            Node<K, V> node = oldValues[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % values.length;
    }

    private boolean isEqual(K key1, K key2) {
        return key1 == key2
                    || key1 != null && key1.equals(key2);
    }

    static class Node<K,V> {
        private final K key;
        private V value;
        private Node next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
