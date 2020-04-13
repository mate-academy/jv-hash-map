package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = indexFor(key);

        if (table[index] == null) {
            table[index] = new Node<K, V>(key, value, null);
            size++;
        } else {
            addNodeInQueue(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        int keyIndexInMyTable = indexFor(key);
        V valueReturn = null;
        if (table[keyIndexInMyTable] != null) {
            Node checked = table[keyIndexInMyTable];
            while (checked != null) {
                if (Objects.equals(key, checked.key)) {
                    return (V) checked.value;
                }
                checked = checked.next;
            }
        }
        return valueReturn;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNodeInQueue(K key, V value, int index) {
        Node checked = table[index];
        while (checked != null) {
            if (Objects.equals(key, checked.key)) {
                checked.value = value;
                return;
            }
            if (checked.next != null) {
                checked = checked.next;
            } else {
                break;
            }
        }
        checked.next = new Node<K, V>(key, value, null);
        size++;
    }

    private int indexFor(K key) {
        if (key == null) {
            return 0;
        }
        int code = key.hashCode();
        code ^= (code >>> 20) ^ (code >>> 12);
        code = code ^ (code >>> 7) ^ (code >>> 4);
        return code & (table.length - 1);
    }

    private void resize() {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * 2];

            for (int i = 0; i < oldTable.length; i++) {
                Node<K, V> node = oldTable[i];
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
