package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int CAPACITY_GROW_FACTOR = 2;

    private Node<K, V>[] table;
    private double loadFactor;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = Math.floorMod(hash, table.length);
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.hash == hash
                        && (current.key == key
                        || (current.key != null
                        && current.key.equals(key)))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = new Node<>(hash, key, value, null);
            size++;
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = Math.floorMod(hash, table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash
                    && (current.key == key
                    || (current.key != null
                    && current.key.equals(key)))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * CAPACITY_GROW_FACTOR;
        threshold = (int) (newCapacity * loadFactor);
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = Math.floorMod(node.hash, newCapacity);
                node.next = table[index];
                table[index] = node;
                size++;
                node = next;
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
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

