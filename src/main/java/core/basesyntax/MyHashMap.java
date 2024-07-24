package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = (hash(key) & 0x7FFFFFFF) % table.length;
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> existingNode = table[index];
        if (existingNode == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = existingNode;
            while (true) {
                if (current.key == null ? key == null : current.key.equals(key)) {
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
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int index = (hash(key) & 0x7FFFFFFF) % table.length;
        Node<K,V> node = table[index];
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

    private void putForNullKey(V value) {
        Node<K,V> current = table[0];
        while (current != null) {
            if (current.key == null) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(null,value,table[0]);
        table[0] = newNode;
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    private V getForNullKey() {
        Node<K, V> current = table[0];
        while (current != null) {
            if (current.key == null) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode() & 0x7FFFFFFF;
    }

    private void resize() {
        int newCapacity = table.length * DEFAULT_MULTIPLIER;
        Node<K,V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);
        for (Node<K,V> node : table) {
            while (node != null) {
                Node<K,V> next = node.next;
                int index = (hash(node.key) & 0x7FFFFFFF) % newCapacity;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }
}
