package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }
        int hash = hash(key);
        int index = hash % table.length;
        if (index < 0) {
            index += table.length;
        }
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, hash, null);
            size++;
            return;
        }
        Node<K, V> current = node;
        while (true) {
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value, hash, null);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        int hash = hash(key);
        int index = hash % table.length;
        if (index < 0) {
            index += table.length;
        }
        Node<K, V> current = table[index];
        while (current != null) {
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
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

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = current.hash % newCapacity;
                if (newIndex < 0) {
                    newIndex += newCapacity;
                }
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode());
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
