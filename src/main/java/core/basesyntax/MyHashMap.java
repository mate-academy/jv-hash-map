package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K,V>[] baskets;
    private int currentCapacity;

    public MyHashMap() {
        currentCapacity = INITIAL_CAPACITY;
        baskets = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * currentCapacity) {
            resize();
        }
        Node<K,V> inputNode = new Node<>(key, value, null);
        int basketIndex = getBasketIndex(key);
        Node<K,V> nodeInBasket = baskets[basketIndex];

        if (nodeInBasket == null) {
            baskets[basketIndex] = inputNode;
            size++;
        } else {
            while (nodeInBasket.next != null) {
                if (Objects.equals(key, nodeInBasket.key)) {
                    nodeInBasket.value = value;
                    return;
                }
                nodeInBasket = nodeInBasket.next;
            }
            if (Objects.equals(key, nodeInBasket.key)) {
                nodeInBasket.value = value;
            } else {
                nodeInBasket.next = inputNode;
                size++;
            }
        }

    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = baskets[getBasketIndex(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        currentCapacity *= 2;
        size = 0;
        Node<K,V>[] previous = baskets;
        baskets = new Node[currentCapacity];
        for (Node<K,V> node : previous) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBasketIndex(K key) {
        return key == null ? 0 : (Math.abs(key.hashCode()) % currentCapacity);
    }

    private static class Node<K,V> {
        final K key;
        V value;
        Node<K, V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
