package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int initialCapacity = 16;
    private float loadFactor = 0.75f;

    private Node<K, V>[] table;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new Node[initialCapacity];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if ((float) size / table.length >= loadFactor) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> current = (Node<K, V>) table[index];

        while (current != null) {
            if (isEqual(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        table[index] = new Node<>(key, value, (Node<K, V>) table[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = (Node<K, V>) table[index];

        while (current != null) {
            if (isEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = (Node<K, V>[]) table;
        table = new Node[oldTable.length * 2];

        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> current = oldTable[i];

            while (current != null) {
                put((K) current.key, (V) current.value);
                current = current.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }

        return Math.abs(key.hashCode()) % table.length;
    }

    private boolean isEqual(K k1, K k2) {
        if (k1 == k2) {
            return true;
        }

        if (k1 == null || k2 == null) {
            return false;
        }

        return k1.equals(k2);
    }
}
