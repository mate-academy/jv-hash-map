package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16; // aka 16
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private static int size;
    private static int threshold;

    @Override
    public void put(K key, V value) {
        if (table.length == 0 || size >= threshold) {
            reSize();
        }
        Node<K, V> newNode = new Node(key.hashCode(), key, value, null);
        int index = hash(key);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                } else if (current.next == null) {
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
        int index = hash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
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

    public int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode() % table.length);
    }

    public void reSize() {
        if (table.length == 0) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
        } else if (size >= threshold) {
            table = reWrite(table);
        }
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    public Node<K, V>[] reWrite(Node<K, V>[] table) {
        int length = table.length * 2;
        Node<K, V>[] newTable = new Node[length];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                int newIndex = node.hash % newTable.length;
                Node<K, V> nextNode = node.next;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = nextNode;
            }
        }
        return newTable;
    }


    public static class Node<K, V> {
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
