package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndexByHash(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = getNode(index, key);
            if (currentNode != null) {
                currentNode.value = value;
            } else {
                table[index] = new Node<>(key, value, table[index]);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(getIndexByHash(key), key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V> getNode(int index, K key) {
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        while (currentNode != null) {
            if (isEqualKey(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int getIndexByHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean isEqualKey(K key1, K key2) {
        return key1 == null ? key2 == null : key1.equals(key2);
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
