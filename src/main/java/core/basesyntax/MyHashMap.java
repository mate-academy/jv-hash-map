package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (LOAD_FACTOR * capacity);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = Math.abs(getHashCode(newNode.key)) % capacity;
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        if (equalsKey(key, table[index].key)) {
            table[index].value = value;
            return;
        }
        Node<K, V> node = table[index];
        while (node.next != null) {
            if (equalsKey(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        node.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(getHashCode(key)) % capacity;
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
        capacity = capacity * 2;
        Node<K, V>[] reserveTable = table;
        table = new Node[capacity];
        threshold = (int) (LOAD_FACTOR * capacity);
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

    private int getHashCode(K key) {
        return 31 * ((key == null) ? 0 : key.hashCode());
    }

    private boolean equalsKey(K key1, K key2) {
        return key1 != null && key1.equals(key2) || key1 == key2;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
