package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private Node[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = convertToHash(key);
        if (table[index] == null) {
            size++;
            table[index] = new Node(key, value, null);
        } else {
            Node<K, V> copyOfNode = table[index];
            while (copyOfNode != null) {
                if (key == copyOfNode.key || key.equals(copyOfNode.key)) {
                    copyOfNode.value = value;
                    return;
                }
                copyOfNode = copyOfNode.next;
            }
            Node<K, V> newElementsArray = new Node<>(key, value, table[index]);
            table[index] = newElementsArray;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return (V) table[0].value;
        }
        int index = convertToHash(key);
        Node<K, V> newNode = table[index];
        while (newNode != null) {
            if (key.equals(newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int convertToHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length + 1;
    }

    private void resize() {
        size = 0;
        Node[] oldMap = table;
        table = new Node[capacity * 2];
        for (Node<K, V> newMap : oldMap) {
            while (newMap != null) {
                put(newMap.key, newMap.value);
                newMap = newMap.next;
            }
        }
        capacity *= 2;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node next;

        Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
