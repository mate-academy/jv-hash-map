package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    static final float LOAD_FACTOR_DEFAULT = 0.75f;
    static final int CAPACITY = 16;
    private Node<K, V> [] table;
    private int size;

    public MyHashMap() {
        table = new Node[CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        if (key == null) {
            if (table[0] == null) {
                table[0] = new Node<K,V>(null, value, null);
                size++;
            } else {
                addNodeInList(null, value, 0);
            }
        } else {
            if (table[indexFor(key.hashCode())] == null) {
                table[indexFor(key.hashCode())] = new Node<K, V>(key, value, null);
                size++;
            } else {
                addNodeInList(key, value, indexFor(key.hashCode()));
            }
        }
    }

    @Override
    public V getValue(K key) {
        int keyIndex;
        keyIndex = (key != null) ? indexFor(key.hashCode()) : 0;
        V valueReturn = null;
        if (table[keyIndex] != null) {
            Node<K, V> checked = table[keyIndex];
            while (checked != null) {
                if (Objects.equals(key, checked.key)) {
                    valueReturn = checked.value;
                    checked = checked.next;
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

    private void resize() {
        if (size == (int)table.length * LOAD_FACTOR_DEFAULT) {
            size = 0;
            Node<K, V>[] tempTable = table;
            table = new Node[table.length * 2];
            for (Node<K, V> tempNode: tempTable) {
                while (tempNode != null) {
                    put(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                }
            }
        }
    }

    private void addNodeInList(K key, V value, int index) {
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                break;
            }
        }
        currentNode.next = new Node<K, V>(key, value, null);
        size++;
    }

    private int indexFor(int keyHashCode) {
        keyHashCode ^= (keyHashCode >>> 20) ^ (keyHashCode >>> 12);
        keyHashCode = keyHashCode ^ (keyHashCode >>> 7) ^ (keyHashCode >>> 4);
        return keyHashCode & (table.length - 1);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
