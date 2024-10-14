package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTORY = 0.75f;
    private int initialCapacity = 16;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[initialCapacity];
    }

    @Override
    public void put(K key, V value) {
        threshold = (int) (initialCapacity * LOAD_FACTORY);
        if (size >= threshold) {
            resize();
        }
        int index = Math.abs(hash(key)) % table.length;
        var current = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(index, key, value, null);
            size++;
        } else {
            while (current != null) {
                if (key == null && current.key == null) {
                    current.value = value;
                    return;
                }
                if (key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(index, key, value, null);
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key)) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (key == null && current.key == null) {
                return current.value;
            }
            if (key != null && key.equals(current.key)) {
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

    Node<K, V>[] resize() {
        int newCapacity = initialCapacity * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (int i = 0; i < initialCapacity; i++) {
            Node<K, V> entry = table[i];
            while (entry != null) {
                Node<K, V> next = entry.next;
                int index = Math.abs(hash(entry.key)) % newCapacity;
                entry.next = newTable[index];
                newTable[index] = entry;
                entry = next;
            }
        }
        table = newTable;
        initialCapacity = newCapacity;

        return table;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}



