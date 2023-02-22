package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_MULTIPLIER = 2;
    private int size;

    private Entry<K, V>[] table = new Entry[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        Entry<K, V> newNode = new Entry<>(key, value, null);
        Entry<K, V> oldNode = table[hashingIndex(key)];
        if (oldNode == null) {
            table[hashingIndex(key)] = newNode;
            size++;
            resize();
        } else {
            while (oldNode != null) {
                if (oldNode.key == newNode.key || oldNode.key != null
                        && oldNode.value.equals(newNode.value)) {
                    oldNode.value = newNode.value;
                    break;
                } else if (oldNode.next == null) {
                    oldNode.next = newNode;
                    size++;
                    break;
                }
                oldNode = oldNode.next;
            }
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Entry<K, V> node = table[hashingIndex(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hashingIndex(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode() % table.length);
    }

    public void resize() {
        if (size > table.length * LOAD_FACTOR) {
            Entry<K, V>[] oldTable = table;
            size = 0;
            table = new Entry[oldTable.length * RESIZE_MULTIPLIER];
            for (Entry<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    public class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
