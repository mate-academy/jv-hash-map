package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> node;
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            node = table[index];
            if (node.key == null || node.key.equals(key)) {
                node.value = value;
                return;
            }
            while (node.next != null) {
                node = node.next;
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }
            node = new Node<>(key, value, table[index]);
            table[index] = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].value;
        }
        for (Node<K, V> kvNode : table) {
            if (kvNode != null) {
                if (kvNode.key != null && kvNode.key.equals(key)) {
                    return kvNode.value;
                }
                while (kvNode.next != null) {
                    kvNode = kvNode.next;
                    if (kvNode.key.equals(key)) {
                        return kvNode.value;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0
                : key.hashCode() == 0 ? 1
                : key.hashCode() < 0
                ? -key.hashCode() % table.length
                : key.hashCode() % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = new Node[table.length];
        threshold = (int) (table.length * 2 * DEFAULT_LOAD_FACTOR);
        System.arraycopy(table, 0, oldTable, 0, table.length);
        table = new Node[table.length * 2];
        Node<K, V> node;
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                node = oldTable[i];
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }
}
