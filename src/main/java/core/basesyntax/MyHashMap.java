package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int NULL_KEY_HASH = 0;
    private static final int RESIZE = 2;
    private int size;
    private int threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = findIndex(key);
        if (table[index] != null) {
            Node<K, V> entry = table[index];
            Node<K, V> entryWithTheSameKey = null;
            while (entry != null) {
                if (entry.key == key || (entry.key != null && entry.key.equals(key))) {
                    entryWithTheSameKey = entry;
                    break;
                }
                if (entry.next == null) {
                    break;
                }
                entry = entry.next;
            }
            if (entryWithTheSameKey != null) {
                entry.value = value;
            } else {
                entry.next = createNewNode(key, value);
                size++;
            }
        } else {
            table[index] = createNewNode(key, value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key);
        if (table[index] != null) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (node.key == key || (node.key != null && node.key.equals(key))) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size + 1 > threshold) {
            size = 0;
            Node<K, V>[] array = table;
            table = new Node[table.length * RESIZE];
            threshold = (int) (table.length * LOAD_FACTOR);
            for (Node<K, V> node : array) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int findIndex(K key) {
        return key != null ? Math.abs(key.hashCode() % table.length) : 0;
    }

    private Node<K, V> createNewNode(K key, V value) {
        if (key == null) {
            return new Node<>(NULL_KEY_HASH, key, value,null);
        }
        return new Node<>(key.hashCode(), key, value, null);
    }

    private static class Node<K, V> {
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
