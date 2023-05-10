package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        size = 0;
        threshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            multiplyCapacity();
        }
        place(new Node<>(key, value));
    }

    @Override
    public V getValue(K key) {
        int hashCode = hash(key);
        int index = indexOf(hashCode);
        if (size == 0 || table[index] == null) {
            return null;
        }
        Node<K, V> bucket = table[index];
        if (bucket.hash == hashCode && objectsAreEqual(bucket.key, key)) {
            return bucket.value;
        } else {
            while (bucket != null) {
                if (bucket.hash == hashCode && objectsAreEqual(bucket.key, key)) {
                    return bucket.value;
                }
                bucket = bucket.next;
            }
        }
        return table[index].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexOf(int hash) {
        return Math.abs(hash % table.length);
    }

    private void place(Node<K, V> node) {
        int index = indexOf(node.hash);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            Node<K, V> bucket = table[index];
            while (bucket != null) {
                if (bucket.hash == node.hash && objectsAreEqual(bucket.key, node.key)) {
                    bucket.value = node.value;
                    return;
                }
                if (bucket.next == null) {
                    bucket.next = node;
                    size++;
                }
                bucket = bucket.next;
            }
        }
    }

    private void multiplyCapacity() {
        Node<K, V>[] oldTable = table;
        int oldSize = size;
        size = 0;
        table = new Node[oldTable.length * CAPACITY_MULTIPLIER];
        threshold *= CAPACITY_MULTIPLIER;

        for (int i = 0; i < oldTable.length && oldSize > 0; i++) {
            Node<K, V> bucket = oldTable[i];
            while (bucket != null && oldSize > 0) {
                oldSize--;
                place(new Node<>(bucket.key, bucket.value));
                bucket = bucket.next;
            }
        }
    }

    private boolean objectsAreEqual(K a, K b) {
        return a == b || a != null && a.equals(b);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private class Node<K, V> {
        private final int hash;
        private V value;
        private final K key;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash(key);
        }
    }

}
