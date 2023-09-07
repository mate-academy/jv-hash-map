package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INIT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;


    public MyHashMap() {
        this.table = new Node[DEFAULT_INIT_CAPACITY];
    }

    private static class Node<K, V> {
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

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putNullKey(value);
            return;
        }
        int hash = hash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<K, V> current = table[hash];
            while (current != null) {
                if (current.key != null && current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            current = table[hash];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    private void putNullKey(V value) {
        int hash = 0;
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, null, value, null);
        } else {
            Node<K, V> current = table[hash];
            while (current != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            Node<K, V> newNode = new Node<>(hash, null, value, null);
            newNode.next = table[hash];
            table[hash] = newNode;
        }
        size++;
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }

    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                int hash = newHash(oldNode.key, newCapacity);
                Node<K, V> newNode = new Node<>(hash, oldNode.key, oldNode.value, null);
                if (table[hash] == null) {
                    table[hash] = newNode;
                } else {
                    Node<K, V> current = table[hash];
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newNode;
                }
                oldNode = oldNode.next;
            }
        }
    }

    private int newHash(K key, int newCapacity) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % newCapacity);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return valueForNullKey();
        }
        int hash = hash(key);
        return valueForKey(key, hash);
    }

    private V valueForKey(K key, int hash) {
        Node<K, V> current = table[hash];
        while (current != null) {
            if (current.key != null && current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private V valueForNullKey() {
        int hash = 0;
        Node<K, V> current = table[hash];
        while (current != null) {
            if (current.key == null) {
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

    final int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
