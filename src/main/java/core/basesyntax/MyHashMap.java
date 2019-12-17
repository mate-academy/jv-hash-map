package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private int hash(K key) {
        int index = key.hashCode() & (table.length - 1);
        return (index == 0) ? 1 : index;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] copyTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> node : copyTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        if (key == null) {
            putKeyNull(key, value);
            return;
        }
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else if (table[index].key.equals(key)) {
            table[index].value = value;
            return;
        } else {
            Node<K, V> lastNode = table[index];
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
            lastNode.next = new Node<>(key, value, null);
        }
        size++;
    }

    private void putKeyNull(K key, V value) {
        if (table[0] == null) {
            table[0] = new Node<>(key, value, null);
            size++;
        } else {
            table[0].value = value;
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;

        if (key == null) {
            return table[0].value;
        }

        int index = hash(key);
        if ((index < 0 || index > table.length)
                || table[index] == null) {
            return null;
        }

        if (table[index].key.equals(key)) {
            return table[index].value;
        } else {
            for (Node<K, V> temp = table[index].next; temp != null; temp = temp.next) {
                if (key.equals(temp.key)) {
                    value = temp.value;
                    break;
                }
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
