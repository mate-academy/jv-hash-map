package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPASITY = 16;
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;
    private int sizeCounter;
    private int capacity;
    private Node<K, V>[] elementsTable;

    public MyHashMap() {
        sizeCounter = 0;
        capacity = DEFAULT_CAPASITY;
        elementsTable = new Node[capacity];
    }

    private static class Node<K, V> {
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (sizeCounter >= capacity * DEFAULT_LOAD_FACTORY) {
            resize();
        }
        int index = indexByKeyHash(key);
        if (elementsTable[index] == null) {
            elementsTable[index] = new Node<>(key, value);
            sizeCounter++;
            return;
        }
        for (int i = index; i <= elementsTable.length; i++) {
            if (i  == elementsTable.length) {
                i = 0;
            }
            if (elementsTable[i] == null) {
                elementsTable[i] = new Node<>(key, value);
                sizeCounter++;
                break;
            }
            if (Objects.equals(elementsTable[i].key, key)) {
                elementsTable[i].value = value;
                return;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexByKeyHash(key);
        if (elementsTable[index] == null) {
            return null;
        }

        for (int i = index; i < elementsTable.length; i++) {
            if (Objects.equals(elementsTable[i].key, key)) {
                return elementsTable[i].value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return sizeCounter;
    }

    private int indexByKeyHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % elementsTable.length - 1);
    }

    private void resize() {
        sizeCounter = 0;
        Node<K, V>[] tempNodeArray = elementsTable;
        capacity *= 2;
        elementsTable = new Node[capacity];
        for (Node<K, V> node : tempNodeArray) {
            if (node == null) {
                continue;
            }
            put(node.key, node.value);
        }
    }
}

