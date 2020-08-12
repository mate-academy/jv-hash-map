package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private float treshhold;

    MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        treshhold = table.length * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size == treshhold) {
            resize();
        }
        Node<K, V> element = getNodeByIndex(key, indexFor(key));
        if (element != null) {
            element.value = value;
        } else {
            Node<K, V> newElement = new Node<>(key, value, table[indexFor(key)]);
            table[indexFor(key)] = newElement;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> element = getNodeByIndex(key, indexFor(key));
        return (element != null) ? element.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] temp = table;
        table = new Node[temp.length * 2];
        size = 0;
        for (Node<K, V> node : temp) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int indexFor(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private Node<K, V> getNodeByIndex(K key, int index) {
        Node<K, V> element = table[index];
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                return element;
            }
            element = element.next;
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
