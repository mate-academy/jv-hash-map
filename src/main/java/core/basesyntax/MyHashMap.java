package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_VOLUME = 16;
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
            if (isFilled()) {
                resizeHashesVector();
            }
            int index = calculateIndex(key);
            Node<K, V> entry = vector[index];
            Node<K, V> newNode = new Node<>(key, value, index);
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
        Node<K, V>[] tempVector = new Node[volume << 1];
        System.arraycopy(vector,0,tempVector,0,vector.length);
        vector = tempVector;
    }

    private boolean isFilled() {
        return (volume * 3 >> 2) < size;
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % volume);
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
        private int hash;

        private Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
