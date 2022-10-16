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
        Node<K, V> e = values[indexByTheKey];
        if (e == null) {
            values[indexByTheKey] = new Node<>(key, value);
            size++;
        } else {
            while (e.next != null) {
                if ((e.key != null) && (e.key.equals(key))) {
                    e.value = value;
                    return;
                }
                if (e.key == null && key == null) {
                    e.value = value;
                    return;
                }
                e = e.next;
            }
            if ((e.key != null) && (e.key.equals(key))) {
                e.value = value;
                return;
            }
            if (e.key == null && key == null) {
                e.value = value;
                return;
            }
            e.next = new Node<>(key, value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int indexByTheKey = getIndexByTheKey(key);
        Node<K, V> e = values[indexByTheKey];
        if (e == null) {
            return null;
        }
        while (e != null) {
            if (key == null && e.key == null) {
                return e.value;
            }
            if (key != null && key.equals(e.key)) {
                return e.value;
            }
            e = e.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] newTable = new Node[capacity * 2];
        for (Node<K, V> e : values) {
            if (e != null) {
                while (e.next != null) {
                    transfer(e.key, e.value, newTable);
                    e = e.next;
                }
                transfer(e.key, e.value, newTable);
            }
        }
        values = newTable;
        capacity *= 2;
    }

    private void transfer(K key, V value, Node<K, V>[] newTable) {
        int hash = (key == null) ? 0 : (key.hashCode() % (capacity * 2));
        hash = hash < 0 ? -hash : hash;
        Node<K, V> e = newTable[hash];
        if (e == null) {
            newTable[hash] = new Node<>(key, value);
        } else {
            while (e.next != null) {
                if ((e.key != null) && (e.key.equals(key))) {
                    e.value = value;
                    return;
                }
                if (e.key == null && key == null) {
                    e.value = value;
                    return;
                }
                e = e.next;
            }
            if ((e.key != null) && (e.key.equals(key))) {
                e.value = value;
                return;
            }
            if (e.key == null && key == null) {
                e.value = value;
                return;
            }
            e.next = new Node<>(key, value);
        }
    }

    private int getIndexByTheKey(K key) {
        int hash = (key == null) ? 0 : (key.hashCode() % capacity);
        return hash < 0 ? -hash : hash;
    }
}
