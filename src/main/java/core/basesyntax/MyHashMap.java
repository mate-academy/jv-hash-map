package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int POSITION_FOR_NULL_KEYS = 0;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
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

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> node = table[getIndex(key)];
        if (node == null) {
            table[getIndex(key)] = newNode;
            size++;
        } else {
            while (node != null) {
                if (key == null && node.key == null
                        || node.key != null && node.key.equals(key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    break;
                }
                node = node.next;
            }
        }
        if (size + 1 > threshold) {
            table = resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (key == null && node.key == null
                    || node.key != null && node.key.equals(key)) {
                return node.value;
            }
            if (node.next != null) {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode() % table.length));
    }

    private Node<K, V>[] resize() {
        final Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = (Node<K, V>[]) (new Node[table.length << 1]);
        table = newTable;
        size = 0;
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> oldNode : oldTable) {
            if (oldNode != null) {
                put(oldNode.key, oldNode.value);
                while (oldNode.next != null) {
                    put(oldNode.next.key, oldNode.next.value);
                    oldNode = oldNode.next;
                }
            }
        }
        return newTable;
    }
}
