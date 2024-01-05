package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_RATE = 2;

    private Node<K, V>[] table;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = createNode(key, value);
        addToTable(node, table, true);
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndexByHash(key == null ? 0 : key.hashCode());
        Node<K, V> result = table[index];
        if (result == null) {
            return null;
        }
        while (result != null) {
            if (keyEquals(result.key, key)) {
                break;
            }
            result = result.next;
        }
        return result == null ? null : result.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> createNode(K key, V value) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        return new Node<>(hashCode, key, value, null);
    }

    private boolean keyEquals(K key1, K key2) {
        return (key1 == key2) || (key1 != null && key1.equals(key2));
    }

    private void increaseSize(boolean increasing) {
        if (increasing) {
            size++;
            if (size >= threshold) {
                resize();
            }
        }
    }

    private int calculateIndexByHash(int hash) {
        return hash & (capacity - 1);
    }

    private void addToTable(Node<K, V> node, Node<K, V>[] table, boolean increase) {
        int index = calculateIndexByHash(node.hash);
        if (table[index] == null) {
            table[index] = node;
            increaseSize(increase);
        } else {
            Node<K, V> nodeWithCollision = table[index];
            while (nodeWithCollision != null) {
                if (keyEquals(nodeWithCollision.key, node.key)) {
                    nodeWithCollision.value = node.value;
                    break;
                }
                if (nodeWithCollision.next == null) {
                    nodeWithCollision.next = node;
                    increaseSize(increase);
                    break;
                }
                nodeWithCollision = nodeWithCollision.next;
            }
        }
    }

    private void resize() {
        capacity = table.length * INCREASE_RATE;
        threshold = threshold * INCREASE_RATE;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : table) {
            if (node != null) {
                while (node != null) {
                    Node<K, V> temp = node.next;
                    node.next = null;
                    addToTable(node, newTable, false);
                    node = temp;
                }
            }
        }
        table = newTable;
    }

    static class Node<K, V> {
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

        public String toString() {
            return key + "=" + value;
        }
    }
}
