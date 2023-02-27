package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_VOLUME = 16;
    private static final double LOAD_FACTOR = 0.75f;
    private Node<K, V>[] vector;
    private int volume;
    private int size;

    public MyHashMap() {
        vector = new Node[INITIAL_VOLUME];
        volume = INITIAL_VOLUME;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> point = findByKey(key);
        if (point == null) {
            if ((volume * LOAD_FACTOR) <= size) {
                resizeHashesVector();
            }
            int index = calculateIndex(key);
            Node<K, V> entry = vector[index];
            Node<K, V> newNode = new Node<>(key, value);
            if (entry == null) {
                vector[index] = newNode;
            } else {
                while (entry != null) {
                    if (entry.next == null) {
                        entry.next = newNode;
                        break;
                    }
                    entry = entry.next;
                }
            }
            size++;
        } else {
            point.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> entry = findByKey(key);
        return entry != null ? entry.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeHashesVector() {
        volume <<= 1;
        Node<K, V>[] tempVector = vector;
        vector = new Node[volume];
        size = 0;
        for (Node<K, V> entry : tempVector) {
            while (entry != null) {
                put(entry.key,entry.value);
                entry = entry.next;
            }
        }
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % vector.length);
    }

    private Node<K, V> findByKey(K key) {
        int index = calculateIndex(key);
        if (vector[index] != null) {
            Node<K, V> entry = vector[index];
            while (entry != null) {
                if ((key == null && entry.key == key)
                        || (key != null && (key == entry.key || key.equals(entry.key)))) {
                    return entry;
                }
                entry = entry.next;
            }
        }
        return null;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
