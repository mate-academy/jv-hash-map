package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int size;

    private static class Node<K, V> {
        private final K key;
        private final V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == (int) (table.length * LOAD_FACTOR)) {
            resize();
        }
        if (putNewValue(getIndex(key), key, value)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if ((node.key != null
                    && node.key.equals(key))
                    || node.key == key) {
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

    private void resize() {
        int newTableLength = table.length * 2;
        Node<K,V>[] newTable = new Node[newTableLength];
        Node<K, V>[] tableCopy = table;
        table = newTable;
        for (Node<K, V> node : tableCopy) {
            while (node != null) {
                putNewValue(getIndex(node.key), node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean putNewValue(int hash, K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        if (table[hash] == null) {
            table[hash] = node;
        } else {
            Node<K, V> previousNode = null;
            Node<K, V> currentNode = table[hash];
            while (currentNode != null) {
                if ((currentNode.key != null
                        && currentNode.key.equals(node.key))
                        || currentNode.key == key) {
                    node.next = currentNode.next;
                    if (previousNode == null) {
                        table[hash] = node;
                    } else {
                        previousNode.next = node;
                    }
                    return false;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;

            }
            previousNode.next = node;
        }
        return true;
    }

    private int getIndex(K key) {
        if (key != null) {
            int hash = Math.abs(key.hashCode());
            hash = hash % table.length;
            return hash;
        }
        return 0;
    }
}
