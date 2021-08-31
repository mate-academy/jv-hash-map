package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    private int size = 0;
    private int currentCapacity = DEFAULT_INITIAL_CAPACITY;

    public static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = 0;
        int index = 0;
        if (key != null) {
            hash = key.hashCode();
            hash = hash < 0 ? -hash : hash;
        }
        index = hash % currentCapacity;
        Node<K, V> newNode = new Node<>(key, value, hash, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            checkingNodes(table[index], newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int index = 0;
        if (key != null) {
            int hash = Math.abs(key.hashCode());
            index = hash % currentCapacity;
        }
        Node<K, V> node = table[index];
        return findingNodes(key, node);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkingNodes(Node<K, V> tableIndex, Node<K, V> newNode) {
        if ((tableIndex.key == null && newNode.key == null)
                || (tableIndex.key != null && tableIndex.key.equals(newNode.key))) {
            tableIndex.value = newNode.value;
            size--;
        } else if (tableIndex.next == null) {
            tableIndex.next = newNode;
        } else {
            checkingNodes(tableIndex.next, newNode);
        }
    }

    private void resize() {
        if (++size > currentCapacity * DEFAULT_LOAD_FACTOR) {
            currentCapacity = currentCapacity * 2;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[currentCapacity];
            size = 1;
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    put(node.key, node.value);
                    while (node.next != null) {
                        put(node.next.key, node.next.value);
                        node = node.next;
                    }
                }

            }
        }
    }

    private V findingNodes(K key, Node<K, V> node) {
        if (node == null) {
            return null;
        }
        if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
            return node.value;
        }
        return findingNodes(key, node.next);
    }
}
