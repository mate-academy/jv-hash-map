package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_SIZE = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        Node<K, V>[] thisTable;
        Node<K, V> tableNode;
        int tableLength;
        if ((thisTable = table) == null || (tableLength = thisTable.length) == 0) {
            tableLength = (thisTable = resize()).length;
        }
        int index = indexFor(hash(key), tableLength);
        if ((tableNode = thisTable[index]) == null) {
            thisTable[index] = newNode(hash(key), key, value);
        } else {
            Node<K, V> node;
            if (tableNode.key != null && tableNode.key.equals(key) || tableNode.key == key) {
                node = tableNode;
            } else {
                for ( ; ; ) {
                    if ((node = tableNode.next) == null) {
                        tableNode.next = newNode(hash(key), key, value);
                        break;
                    }
                    if (node.key != null && node.key.equals(key) || node.key == key) {
                        break;
                    }
                    tableNode = node;
                }
            }
            if (node != null) {
                node.value = value;
                return;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V>[] thisTable;
        if ((thisTable = table) == null || thisTable.length == 0) {
            return null;
        }
        int index = indexFor(hash(key),thisTable.length);
        for (Node<K, V> node = thisTable[index]; node != null; node = node.next) {
            if (node.key != null && node.key.equals(key) || node.key == key) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity = INITIAL_SIZE;
        int newThreshold = (int) (LOAD_FACTOR * INITIAL_SIZE);
        if (oldCapacity > 0) {
            newCapacity = oldCapacity << 1;
            newThreshold = oldThreshold << 1;
        }
        threshold = newThreshold;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        if (oldTable != null) {
            transfer(newTable);
        }
        table = newTable;
        return newTable;
    }

    private Node<K, V> newNode(int hash, K key, V value) {
        return new Node<>(hash, key, value, null);
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = newTable.length;
        for (int i = 0; i < oldCapacity; ++i) {
            Node<K, V> node = oldTable[i];
            if (node != null) {
                oldTable[i] = null;
                do {
                    Node<K,V> next = node.next;
                    int index = indexFor(node.hash, newCapacity);
                    node.next = newTable[index];
                    newTable[index] = node;
                    node = next;
                } while (node != null);
            }
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static int indexFor(int hashCode, int arrayLength) {
        return (hashCode & (arrayLength - 1));
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && key == node.key || key != null && key.equals(node.key)
                    && value == node.value || value != null && value.equals(node.value)
                    && next == node.next || next != null && next.equals(node.next);
        }

        @Override
        public final int hashCode() {
            int result = 17;
            result = 31 * result + (value == null ? 0 : value.hashCode());
            result = 31 * result + (key == null ? 0 : key.hashCode());
            result = 31 * result + (next == null ? 0 : next.hashCode());
            return result;
        }
    }
}
