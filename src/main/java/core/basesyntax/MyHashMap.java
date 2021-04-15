package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_MULTIPLICATION_FACTOR = 2;

    private int size;
    private int threshold;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    public Node<K, V> getNode(K key) {
        int position = getPositionByKey(key);
        Node<K, V> current = table[position];
        if (current == null) {
            return null;
        }
        while (current != null) {
            if (current.key == key || key != null && key.equals(current.key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }

        int position = getPositionByKey(key);
        Node<K, V> current = getNode(key);
        if (current != null) {
            current.value = value;
            return;
        }
        table[position] = new Node<>(key, value, table[position]);
        size++;
    }

    @Override
    public V getValue(K key) {
        return getNode(key) == null ? null : getNode(key).value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final Node<K, V>[] oldArray = table;
        table = new Node[table.length * DEFAULT_MULTIPLICATION_FACTOR];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        Node<K, V> current;
        size = 0;
        for (Node<K, V> node : oldArray) {
            current = node;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getPositionByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
