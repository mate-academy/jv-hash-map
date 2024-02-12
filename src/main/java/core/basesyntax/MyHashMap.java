package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }
        int hash = hash(key);
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = new Node<>(key, value, null);
        } else {
            Node<K, V> lastNode = null;
            do {
                if (node.key == key || key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    lastNode = node;
                }
                node = node.next;
            } while (node != null);
            lastNode.next = new Node<>(key, value, null);
        }
        if (++size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            Node<K, V> node = table[hash(key)];
            while (node != null) {
                if (node.key == key || key != null && key.equals(node.key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
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

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private void resize() {
        int newCapacity = 0;
        if (table == null) {
            newCapacity = DEFAULT_CAPACITY;
        } else {
            newCapacity = table.length * GROW_FACTOR;
        }
        Node<K, V>[] oldTable = table;
        @SuppressWarnings("unchecked")
        Node<K, V>[] newTable = (Node<K,V>[]) new Node[newCapacity];
        table = newTable;
        transfer(oldTable);
    }

    private void transfer(Node<K,V>[] oldTable) {
        if (oldTable != null) {
            size = 0;
            for (int j = 0; j < oldTable.length; j++) {
                Node<K, V> node = oldTable[j];
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
