package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> keyNode = findNodeByKey(key);
        if (keyNode == null) {
            int basket = getHash(key) % table.length;
            table[basket] = new Node<>(key, value, table[basket]);
            size++;
        } else {
            keyNode.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> searchedNode = findNodeByKey(key);
        return searchedNode == null ? null : searchedNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public V remove(K key) {
        int basket = getHash(key) % table.length;
        Node<K, V> previousBasketNode = null;
        Node<K, V> currentBasketNode = table[basket];
        while (currentBasketNode != null) {
            if (Objects.equals(currentBasketNode.key, key)) {
                if (previousBasketNode != null) {
                    previousBasketNode.next = currentBasketNode.next;
                } else {
                    table[basket] = currentBasketNode.next;
                }
                size--;
                return currentBasketNode.value;
            }
            previousBasketNode = currentBasketNode;
            currentBasketNode = currentBasketNode.next;
        }
        return null;
    }

    public boolean remove(K key, V value) {
        Node<K, V> searchedNode = findNodeByKey(key);
        if (searchedNode != null && searchedNode.value.equals(value)) {
            remove(key);
            return true;
        }
        return false;
    }

    public boolean containsKey(K key) {
        return findNodeByKey(key) != null;
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode());
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> basketNode : oldTable) {
            while (basketNode != null) {
                put(basketNode.key, basketNode.value);
                basketNode = basketNode.next;
            }
        }
    }

    private Node<K, V> findNodeByKey(K key) {
        Node<K, V> currentBasketNode = table[getHash(key) % table.length];
        while (currentBasketNode != null) {
            if (Objects.equals(currentBasketNode.key, key)) {
                return currentBasketNode;
            }
            currentBasketNode = currentBasketNode.next;
        }
        return null;
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
