package core.basesyntax;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size = 0;
    private int capacity = INITIAL_CAPACITY;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if ((size + 1) > capacity * LOAD_FACTOR) {
            resize();
        }

        int hash = hash(key);
        int index = indexFor(hash, capacity);

        Node<K, V> head = table[index];
        if (head == null) {
            table[index] = new Node<>(key, value, null, hash);
            size++;
            return;
        }

        Node<K, V> current = head;
        while (true) {
            if (equalsKey(current, key, hash)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value, null, hash);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, capacity);

        Node<K, V> node = table[index];
        while (node != null) {
            if (equalsKey(node, key, hash)) {
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

    private void resize() {
        int newCapacity = capacity << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = indexFor(node.hash, newCapacity);

                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    private int hash(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        h ^= (h >>> 16);
        return h;
    }

    private int indexFor(int hash, int cap) {
        return hash & (cap - 1);
    }

    private boolean equalsKey(Node<K, V> node, K key, int hash) {
        if (node.hash != hash) {
            return false;
        }
        if (node.key == key) {
            return true;
        }
        return node.key != null && node.key.equals(key);
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;
        int hash;

        Node(K key, V value, Node<K, V> next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
