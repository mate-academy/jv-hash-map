package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value);
        int position = newNode.key != null ? Math.abs(newNode.key.hashCode()) % table.length : 0;
        if (table[position] != null) {
            Node<K, V> existingNode = table[position];
            while (existingNode.next != null) {
                if (validateKeys(existingNode.key, newNode.key)) {
                    existingNode.value = newNode.value;
                    return;
                } else {
                    existingNode = existingNode.next;
                }
            }
            if (validateKeys(existingNode.key, newNode.key)) {
                existingNode.value = newNode.value;
            } else {
                existingNode.next = newNode;
                size++;
            }
        } else {
            table[position] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> searchingNode = table[key != null
                ? Math.abs(key.hashCode()) % table.length : 0];
        if (searchingNode != null) {
            while (searchingNode.next != null) {
                if (validateKeys(searchingNode.key, key)) {
                    return searchingNode.value;
                } else {
                    searchingNode = searchingNode.next;
                }
            }
            if (validateKeys(searchingNode.key, key)) {
                return searchingNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            int newCapacity = table.length << 1;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[newCapacity];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private boolean validateKeys(K keyOne, K keyTwo) {
        return keyOne == keyTwo || keyOne != null && keyOne.equals(keyTwo);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
