package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_SIZE = 0;
    private static final int CAPACITY_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
        this.size = DEFAULT_SIZE;
    }

    @Override
    public void put(K key, V value) {
        int bucket = hashCode(key) & (capacity - 1);
        Node<K, V> node = new Node<>(hashCode(key), key, value, null);
        if (table[bucket] == null) {
            table[bucket] = node;
            size++;
        } else {
            Node<K, V> prev = table[bucket];
            while (prev != null) {
                if (prev.key == null ? key == null : prev.key.equals(key)) {
                    prev.value = value;
                    break;
                } else if (prev.next == null) {
                    prev.next = node;
                    size++;
                    break;
                }
                prev = prev.next;
            }
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        int bucket = hashCode(key) & (capacity - 1);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (node.key == null ? key == null : node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashCode(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        if (size > (capacity * LOAD_FACTOR)) {
            capacity *= CAPACITY_MULTIPLIER;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[capacity];
            size = DEFAULT_SIZE;
            for (Node<K, V> node : oldTable) {
                if (node == null) {
                    continue;
                }
                put(node.key, node.value);
                Node<K, V> prev = node.next;
                while (prev != null) {
                    put(prev.key, prev.value);
                    prev = prev.next;
                }
            }
        }
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

