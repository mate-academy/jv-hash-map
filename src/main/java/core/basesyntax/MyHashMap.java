package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hash = hash(key.hashCode());
        int index = indexFor(hash, table.length);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || key.equals(node.key))) {
                node.value = value;
                return;
            }
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int hash = hash(key.hashCode());
        int index = indexFor(hash, table.length);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || key.equals(node.key))) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        for (Node<K, V> node = table[0]; node != null; node = node.next) {
            if (node.key == null) {
                node.value = value;
                return;
            }
        }
        addNode(0, null, value, 0);
    }

    private V getForNullKey() {
        for (Node<K, V> node = table[0]; node != null; node = node.next) {
            if (node.key == null) {
                return node.value;
            }
        }
        return null;
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * GROW_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                Node<K, V> next = oldNode.next;
                int index = indexFor(oldNode.hash, newTable.length);
                oldNode.next = newTable[index];
                newTable[index] = oldNode;
                oldNode = next;
            }
        }
    }

    private int hash(int hashCode) {
        return hashCode ^ (hashCode >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
