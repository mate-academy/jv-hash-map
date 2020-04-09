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
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
        } else {
            if (table[indexFor(key.hashCode())] == null) {
                table[indexFor(key.hashCode())] = new Node<K, V>(key.hashCode(), key, value, null);
                size++;
            } else {
                addNodeInQueue(key, value, indexFor(key.hashCode()));
            }
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        int keyIndexInMyTable;
        if (key != null) {
            keyIndexInMyTable = indexFor(key.hashCode());
        } else {
            keyIndexInMyTable = 0;
        }
        V valueReturn = null;
        if (checkIndex(keyIndexInMyTable)) {
            Node checked = table[keyIndexInMyTable];
            while (checked != null) {
                if (Objects.equals(key, checked.key)) {
                    return (V) checked.value;
                } else {
                    checked = checked.next;
                }
            }
        }
        return valueReturn;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        if (table[0] == null) {
            table[0] = new Node(111, null, value, null);
            size++;
        } else {
            addNodeInQueue(null, value, 0);
        }
    }

    private int addNodeInQueue(K key, V value, int index) {
        Node checked = table[index];
        while (checked != null) {
            if (Objects.equals(key, checked.key)) {
                checked.value = value;
                return 0;
            }
            if (checked.next != null) {
                checked = checked.next;
            } else {
                break;
            }
        }
        int keyHash = (key == null ? 111 : key.hashCode());
        checked.next = new Node<K, V>(keyHash, key, value, null);
        size++;
        return 1;
    }

    private int indexFor(int code) {
        code ^= (code >>> 20) ^ (code >>> 12);
        code = code ^ (code >>> 7) ^ (code >>> 4);
        return code & (table.length - 1);
    }

    private boolean checkIndex(int index) {
        return table[index] != null;
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
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
