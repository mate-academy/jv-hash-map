package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int PRIME_INTEGER = 17;
    private static final int ENLARGE_TABLE = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > LOAD_FACTOR * table.length) {
            resize();
        }
        int hashCode = hash(key);
        int index = getIndex(hashCode);
        Node<K, V> newNode = new Node<K, V>(key, value, null);
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
    }

    @Override
    public V getValue(K key) {
        int hashCode = hash(key);
        int index = getIndex(hashCode);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
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

    private static <K> int hash(K key) {
        return (key == null) ? 0 : key.hashCode() * PRIME_INTEGER;
    }

    private void resize() {
        int newCapacity = table.length * ENLARGE_TABLE;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node: table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = Math.abs(hash(node.key)) % newCapacity;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        int threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    private int getIndex(int hashCode) {
        return Math.abs(hashCode) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
