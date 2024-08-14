package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_BASE_SIZE = 16;
    private static final float RESIZE_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[])(new Object[DEFAULT_BASE_SIZE]);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int position = calculateIndex(key);
        if (table[position] == null) {
            table[position] = new Node<>(key, value, hash(key), null);
            size++;
            return;
        }

        Node<K, V> current = (Node<K, V>) table[position];

        while (current.next != null) {
            if (areEqual(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        if (areEqual(current.key, key)) {
            current.value = value;
            return;
        }
        current.next = new Node<K, V>(key, value, hash(key), null);
        size++;

    }

    @Override
    public V getValue(K key) {
        int position = calculateIndex(key);
        if (table[position] == null) {
            return null;
        } else {
            Node<K, V> current = (Node<K, V>) table[position];
            while (current != null) {
                if (current.hash == hash(key)
                        && areEqual(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateIndex(K key) {
        return hash(key) % table.length;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        if (size >= (int) (table.length * RESIZE_FACTOR)) {
            Node<K, V>[] newTable = (Node<K, V>[])(new Object[table.length * RESIZE_COEFFICIENT]);
            for (int i = 0; i < table.length; i++) {
                Node<K, V> node = table[i];
                while (node != null) {
                    int newPosition = node.hash % newTable.length;
                    node = rehash(node, newTable, newPosition);
                }
            }
            table = newTable;
        }
    }

    private Node<K, V> rehash(Node<K, V> node, Object[] newTable, int newPosition) {
        Node<K, V> next = node.next;
        node.next = (Node<K, V>) newTable[newPosition];
        newTable[newPosition] = node;
        return next;
    }

    private boolean areEqual(Object object1, Object object2) {
        if (object1 == null || object2 == null) {
            return object1 == object2;
        }
        return object1.equals(object2);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, int hash, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
