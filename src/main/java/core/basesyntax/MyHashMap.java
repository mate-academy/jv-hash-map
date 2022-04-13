package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (LOAD_FACTOR * table.length == size) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        if (table[hash(key)] == null) {
            table[hash(key)] = newNode;
        } else {
            Node<K, V> iterator = table[hash(key)];
            while (iterator != null) {
                if (iterator.key == key || iterator.key != null
                        && iterator.key.equals(key)) {
                    iterator.value = value;
                    return;
                }
                if (iterator.next == null) {
                    break;
                }
                iterator = iterator.next;
            }
            iterator.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> bucket : table) {
            if (hash(key) == hash(key)) {
                Node<K, V> iterator = table[hash(key)];
                while (iterator != null) {
                    if (iterator.key == key || iterator.key != null
                            && iterator.key.equals(key)) {
                        return iterator.value;
                    }
                    iterator = iterator.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        for (Node<K, V> bucket : oldTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
