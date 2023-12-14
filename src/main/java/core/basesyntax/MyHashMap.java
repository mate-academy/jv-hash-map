package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] values;
    private int size;

    public MyHashMap() {
        values = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int index = createIndex(key);
        Node<K, V> current = values[index];
        while (current != null) {
            if (equalsKeys(key, current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        if (values[index] == null) {
            values[index] = new Node<>(key, value);
        } else {
            current = values[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexToFind = createIndex(key);
        Node<K, V> node = values[indexToFind];
        while (node != null) {
            if (equalsKeys(key, node.key)) {
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

    private int createIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % values.length);
    }

    private boolean equalsKeys(K k1, K k2) {
        return k1 == k2 || k1 != null && k1.equals(k2);
    }

    private void transfer() {
        Node<K, V>[] oldArray = values;
        values = new Node[oldArray.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void checkSize() {
        int threshold = (int) (values.length * LOAD_FACTOR);
        if (size == threshold) {
            transfer();
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
