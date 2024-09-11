package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int SHIFT_BITS = 16;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;
    private int threshold;

    public MyHashMap() {
        this.capacity = 16;
        this.table = (Node<K, V>[]) new Node[capacity];
        this.loadFactor = 0.75f;
        this.threshold = (int) (capacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int hashCode = hash(key);
        int index = Math.abs(hashCode) % table.length;
        Node<K, V> newNode = new Node<>(hashCode, key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (true) {
                if (current.key == null && key == null) {
                    current.value = value;
                    return;
                }
                if (current.key != null && current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hashCode = hash(key);
        int index = Math.abs(hashCode) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == null && key == null) {
                return current.value;
            }
            if (current.key != null && current.key.equals(key)) {
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

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> SHIFT_BITS);
    }

    public void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node: table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = Math.abs(node.hash) % newCapacity;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    class Node<K, V> {
        private int hash;
        private K key;
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
