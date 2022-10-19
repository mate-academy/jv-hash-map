package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;
    private Node<K, V>[] values;

    public MyHashMap() {
        values = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        int indexByTheKey = getIndexByKey(key);
        Node<K, V> node = values[indexByTheKey];
        if (node == null) {
            values[indexByTheKey] = new Node<>(key, value);
            size++;
        } else {
            while (node != null) {
                if ((node.key != null) && (node.key.equals(key))
                        || (node.key == null && key == null)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value);
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int indexByTheKey = getIndexByKey(key);
        Node<K, V> node = values[indexByTheKey];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
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

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        size = 0;
        Node<K, V>[] oldValuesMap = values;
        values = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> value : oldValuesMap) {
            while (value != null) {
                put(value.key, value.value);
                value = value.next;
            }
        }
    }

    private int getIndexByKey(K key) {
        return Math.abs((key == null) ? 0 : (key.hashCode() % capacity));
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;


        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
