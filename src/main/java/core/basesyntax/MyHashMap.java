package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_BASE_SIZE = 16;
    private static final float RESIZE_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;

    private Object[] table;
    private int size;

    public MyHashMap() {
        table = new Object[DEFAULT_BASE_SIZE];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int position = position(key);
        if (table[position] == null) {
            table[position] = new Node<K, V>(key, value, hash(key), null);
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
        int position = position(key);
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

    private int position(K key) {
        return hash(key) % table.length;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        if (size >= (int) (table.length * RESIZE_FACTOR)) {
            Object[] newTable = new Object[table.length * RESIZE_COEFFICIENT];
            for (int i = 0; i < table.length; i++) {
                Node<K, V> node = (Node<K, V>) table[i];
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

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
