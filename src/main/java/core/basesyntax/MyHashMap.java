package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;
    private Node<K, V>[] values;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        values = new Node[DEFAULT_INITIAL_CAPACITY];
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

    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        int indexByTheKey = getIndexByTheKey(key);
        Node<K, V> node = values[indexByTheKey];
        if (node == null) {
            values[indexByTheKey] = new Node<>(key, value);
            size++;
        } else {
            while (node.next != null) {
                if ((node.key != null) && (node.key.equals(key))) {
                    node.value = value;
                    return;
                }
                if (node.key == null && key == null) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if ((node.key != null) && (node.key.equals(key))) {
                node.value = value;
                return;
            }
            if (node.key == null && key == null) {
                node.value = value;
                return;
            }
            node.next = new Node<>(key, value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int indexByTheKey = getIndexByTheKey(key);
        Node<K, V> node = values[indexByTheKey];
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (key == null && node.key == null) {
                return node.value;
            }
            if (key != null && key.equals(node.key)) {
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
        Node<K, V>[] newTable = new Node[capacity];
        for (Node<K, V> value: values) {
            if (value != null) {
                while (value.next != null) {
                    transfer(value.key, value.value, newTable);
                    value = value.next;
                }
                transfer(value.key, value.value, newTable);
            }
        }
        values = newTable;
    }

    private void transfer(K key, V value, Node<K, V>[] newTable) {
        int indexByTheKey = getIndexByTheKey(key);
        Node<K, V> node = newTable[indexByTheKey];
        if (node == null) {
            newTable[indexByTheKey] = new Node<>(key, value);
        } else {
            while (node.next != null) {
                if ((node.key != null) && (node.key.equals(key))) {
                    node.value = value;
                    return;
                }
                if (node.key == null && key == null) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if ((node.key != null) && (node.key.equals(key))) {
                node.value = value;
                return;
            }
            if (node.key == null && key == null) {
                node.value = value;
                return;
            }
            node.next = new Node<>(key, value);
        }
    }

    private int getIndexByTheKey(K key) {
        int index = (key == null) ? 0 : (key.hashCode() % capacity);
        return index < 0 ? -index : index;
    }
}
