package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_CAPACITY = 16;
    private static final double WORKLOAD = 0.75;
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
        putForNonNullKey(getIndex(key), key, value);
    }

    @Override
    public V getValue(K key) {
        return valueForKey(key);
    }

    @Override
    public int getSize() {
        return size;
    }

    final int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private V valueForKey(K key) {
        if (key == null) {
            int index = 0;
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void putForNonNullKey(int index, K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
        if ((double) size / table.length > WORKLOAD) {
            resize();
        }
    }

    private void putForNullKey(V value) {
        int index = 0;
        if (table[index] == null) {
            table[index] = new Node<>(null, value);
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            Node<K, V> newNode = new Node<>(null, value);
            newNode.next = table[index];
            table[index] = newNode;
        }
        size++;
        if ((double) size / table.length > 0.75) {
            resize();
        }
    }
}
