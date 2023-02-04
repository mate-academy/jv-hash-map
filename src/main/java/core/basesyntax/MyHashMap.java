package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }

        int indexOfBucket = hash(key);

        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = new Node(indexOfBucket, key, value, null);
            size++;
        }

        Node<K, V> pointer = table[indexOfBucket];

        while (pointer != null) {
            if (pointer.key == key || key != null && key.equals(pointer.key)) {
                pointer.value = value;
                break;
            }
            if (pointer.next == null) {
                pointer.next = new Node(indexOfBucket, key, value, null);
                size++;
                break;
            } else {
                pointer = pointer.next;
            }
        }

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int indexOfBucket = hash(key);
        Node<K, V> pointer = table[indexOfBucket];

        while (pointer != null) {
            if (pointer.key == key || key != null && key.equals(pointer.key)) {
                return pointer.value;
            } else {
                pointer = pointer.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int newCapacity = 0;

        if (oldCapacity > 0) {
            newCapacity = oldCapacity << 1;
            threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        } else if (oldCapacity == 0) {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        table = (Node<K, V>[])new Node[newCapacity];

        if (oldTable != null) {
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    public static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
