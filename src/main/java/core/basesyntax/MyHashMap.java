package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private static final int POSITION_FOR_NULL_KEYS = 0;
    private int threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    private int capacity = INITIAL_CAPACITY;
    private int size;
    private Node<K, V>[] hashMap = new Node[INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        resize();
        if (key == null) {
            putIfKeyNull(value);
        } else {
            putIfKeyNotNull(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        int position = 0;
        if (key != null) {
            position = Math.abs(key.hashCode() % capacity);
        }
        Node<K, V> current = hashMap[position];
        if (hashMap[position] == null) {
            return null;
        }
        while (current.key == null || !(current.key.equals(key))) {
            if (current.key == null && key == null) {
                return current.value;
            }
            if (current.next == null) {
                return null;
            }
            current = current.next;
        }
        return current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshold) {
            capacity *= 2;
            threshold *= 2;
            Node<K, V>[] newHashMap = new Node[capacity];
            Node<K, V>[] oldHashMap = hashMap;
            hashMap = newHashMap;
            Node<K, V> current;
            size = 0;
            for (int i = 0; i < capacity / 2; i++) {
                current = oldHashMap[i];
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private boolean checkOnSameKey(Node<K, V> current, K key, V value) {
        if (current.key != null && current.key.equals(key)) {
            current.value = value;
            return true;
        }
        return false;
    }

    private void putIfKeyNotNull(K key, V value) {
        Node<K, V> newNode = new Node<>(key.hashCode(), key, value, null);
        int position = Math.abs(key.hashCode() % capacity);
        if (hashMap[position] == null) {
            hashMap[position] = newNode;
            size++;
            return;
        }
        Node<K, V> current = hashMap[position];
        if (checkOnSameKey(current, key, value)) {
            return;
        }
        while (current.next != null) {
            if (checkOnSameKey(current, key, value)) {
                return;
            }
            current = current.next;
        }
        if (checkOnSameKey(current, key, value)) {
            return;
        }
        current.next = newNode;
        size++;
    }

    private void putIfKeyNull( V value) {
        Node<K, V> newNode = new Node<>(0, null, value, null);
        if (hashMap[POSITION_FOR_NULL_KEYS] == null) {
            hashMap[POSITION_FOR_NULL_KEYS] = newNode;
            size++;
            return;
        }
        Node<K, V> current = hashMap[POSITION_FOR_NULL_KEYS];
        while (current.key != null) {
            if (current.next == null) {
                current.next = newNode;
                size++;
                return;
            }
            current = current.next;
        }
        current.value = value;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private int hash;
        private V value;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
