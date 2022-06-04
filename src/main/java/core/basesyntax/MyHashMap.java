package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int capasity;
    private int size;
    private Node<K, V>[] values;

    @Override
    public void put(K key, V value) {
        resize();
        int hashKey = getHash(key);
        int index = getIndex(key, hashKey);
        if (values[index] == null) {
            values[index] = new Node(hashKey, key, value, null);
            size++;
            return;
        }
        Node<K, V> node = values[index];
        while (node != null) {
            if (isEqual(node.hash, hashKey,node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node(hashKey, key, value, null);
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
        int hashKey = getHash(key);
        int index = getIndex(key, hashKey);
        Node<K, V> node = values[index];
        while (node != null) {
            if (isEqual(node.hash, hashKey,node.key, key)) {
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

    public void resize() {
        if (values == null) {
            capasity = DEFAULT_INITIAL_CAPACITY;
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            values = new Node[capasity];
        }
        if (size > threshold || values == null) {
            capasity = capasity << 1;
            threshold = (int) (capasity * DEFAULT_LOAD_FACTOR);
            moveToNewCapacity();
        }
    }

    public void moveToNewCapacity() {
        Node<K, V>[] oldValues = values;
        values = new Node[capasity];
        size = 0;
        for (int i = 0; i < oldValues.length; i++) {
            Node<K, V> node = oldValues[i];
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key, int hash) {
        return key == null ? 0 : Math.abs(hash) % capasity;
    }

    private boolean isEqual(int hash1, int hash2, K key1, K key2) {
        return hash1 == hash2 && (key1 == key2
                    || key1 != null && key1.equals(key2));
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
