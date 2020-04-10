package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        size = 0;
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        if (addToTable(key, value, table)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeByKey = table[hash(key, table.length)];
        while (nodeByKey != null) {
            if ((key != null && key.equals(nodeByKey.key)) || key == nodeByKey.key) {
                return nodeByKey.value;
            }
            nodeByKey = nodeByKey.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean addToTable(K key, V value, Node<K, V>[] table) {
        int hash = hash(key, table.length);
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
            return true;
        } else {
            Node<K, V> lastNode = table[hash];
            while (lastNode != null) {
                if ((lastNode.key != null && lastNode.key.equals(key))
                        || lastNode.key == key) {
                    lastNode.value = value;
                    break;
                } else if (lastNode.nextNode == null) {
                    lastNode.nextNode = new Node<>(key, value, null);
                    return true;
                }
                lastNode = lastNode.nextNode;
            }
        }
        return false;
    }

    private void resize() {
        if (((float) size) / table.length > LOAD_FACTOR) {
            Node<K,V>[] newTable = (Node<K, V>[]) new Node[table.length * 2];
            for (Node<K, V> node : table) {
                while (node != null) {
                    addToTable(node.key, node.value, newTable);
                    node = node.nextNode;
                }
            }
            table = newTable;
        }
    }

    private int hash(K key, int length) {
        return key != null ? Math.abs(key.hashCode() % length) : 0;
    }

    private static final class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
