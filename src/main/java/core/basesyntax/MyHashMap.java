package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int EMPTY_SIZE = 0;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (replaceExistingNode(key, value)) {
            return;
        }
        if (++size > threshold) {
            resize();
        }
        addNode(key, value);
    }

    @Override
    public V getValue(K key) {
        return (getNode(key) == null) ? null : getNode(key).value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean replaceExistingNode(K key, V value) {
        if (getNode(key) != null) {
            getNode(key).value = value;
            return true;
        }
        return false;
    }

    private void addNode(K key, V value) {
        int index = hash(key);
        if (table[index] != null) {
            Node<K, V> node = table[index];
            while (node.next != null) {
                node = node.next;
            }
            node.next = new Node<>(index, key, value, null);
            return;
        }
        table[index] = new Node<>(index, key, value, null);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * GROW_FACTOR];
        copyAllNodes(oldTable);
    }

    private void copyAllNodes(Node<K, V>[] fromTable) {
        for (Node<K, V> node : fromTable) {
            if (node != null) {
                if (node.next == null) {
                    addNode(node.key, node.value);
                }
                copyFromCollision(node);
            }
        }
    }

    private void copyFromCollision(Node<K,V> next) {
        if (next != null) {
            addNode(next.key, next.value);
            copyFromCollision(next.next);
        }
    }

    private Node<K, V> getNode(K key) {
        if (size != EMPTY_SIZE) {
            int index = hash(key);
            return findInCollision(table[index], key);
        }
        return null;
    }

    private Node<K, V> findInCollision(Node<K, V> node, K key) {
        if (node != null) {
            if ((key == null && node.key == null) || (node.key != null && node.key.equals(key))) {
                return node;
            }
            return findInCollision(node.next, key);
        }
        return null;
    }

    private int hash(K key) {
        int index = (key == null) ? 0 : key.hashCode() % table.length;
        return (index < 0) ? ~index : index;
    }

    private static class Node<K,V> {
        private final int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
