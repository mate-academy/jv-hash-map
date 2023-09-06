package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[START_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hash = hash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode != null) {
                if (currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode = table[hash];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
        if ((double) size / table.length > 0.75) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return valueForNullKey();
        }
        int hash = hash(key);
        return valueForKey(key, hash);
    }

    @Override
    public int getSize() {
        return size;
    }

    final int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
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
                    Node<K, V> currentNode = table[hash];
                    while (currentNode.next != null) {
                        currentNode = currentNode.next;
                    }
                    currentNode.next = newNode;
                }
                oldNode = oldNode.next;
            }
        }
    }

    private int newHash(K key, int newCapacity) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % newCapacity;
    }

    private void putForNullKey(V value) {
        int hash = 0;
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, null, value, null);
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            Node<K, V> newNode = new Node<>(hash, null, value, null);
            newNode.next = table[hash];
            table[hash] = newNode;
        }
        size++;
        if ((double) size / table.length > 0.75) {
            resize();
        }
    }

    private V valueForKey(K key, int hash) {
        Node<K, V> currentNode = table[hash];
        while (currentNode != null) {
            if (currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private V valueForNullKey() {
        int hash = 0;
        Node<K, V> currentNode = table[hash];
        while (currentNode != null) {
            if (currentNode.key == null) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }
}
