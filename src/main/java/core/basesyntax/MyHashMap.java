package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V> [] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) LOAD_FACTOR * table.length;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = hash(key);
        Node<K,V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (equalsKey(current.key, key)) {
                    current.value = value;
                    break;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    break;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table[hash(key)] == null) {
            return null;
        } else if (table[hash(key)].next != null) {
            Node<K, V> current = table[hash(key)];
            while (current != null) {
                if (equalsKey(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }
        return table[hash(key)].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private boolean equalsKey(K firstKey, K secondKey) {
        return firstKey == secondKey || firstKey != null && firstKey.equals(secondKey);
    }

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private void resize() {
        if (size == threshold) {
            Node<K, V>[] newArray = table;
            fillNewArray(newArray);
        }
    }

    private void fillNewArray(Node<K, V>[] newArray) {
        table = (Node<K, V>[]) new Node[table.length << 1];
        size = 0;
        for (Node<K, V> newNode: newArray) {
            Node<K, V> current = newNode;
            if (newNode != null) {
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }
}
