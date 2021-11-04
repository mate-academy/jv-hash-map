package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_PERCENT = 0.75;
    private static final int COEFFICIENT = 2;
    private int load;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        load = (int) (LOAD_PERCENT * capacity);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(hashCode(key), key, value, null);
        if (size + 1 == load) {
            resize();
        }
        int index = Math.abs(node.hash) % capacity;
        Node<K, V> item = table[index];
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        do {
            if (equalsKey(key, item.key)) {
                item.value = value;
                return;
            }
            item = item.next == null ? item : item.next;
        } while (item.next != null);
        item.next = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hashCode(key)) % capacity;
        Node<K, V> item = table[index];
        while (item != null) {
            if (equalsKey(key, item.key)) {
                return item.value;
            }
            item = item.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        capacity = capacity * COEFFICIENT;
        Node<K, V>[] reserveTable = table;
        table = new Node[capacity];
        load = (int) (LOAD_PERCENT * capacity);
        for (Node<K, V> item: reserveTable) {
            if (item != null) {
                put(item.key, item.value);
                while (item.next != null) {
                    item = item.next;
                    put(item.key, item.value);
                }
            }
        }
    }

    private int hashCode(K key) {
        return 31 * ((key == null) ? 0 : key.hashCode());
    }

    private boolean equalsKey(K key1, K key2) {
        return key1 != null && key1.equals(key2) || key1 == key2;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
