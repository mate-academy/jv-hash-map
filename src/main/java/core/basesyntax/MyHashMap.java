package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int currentCapacity;
    private int userCapacity;
    private Node<K, V>[] arrayOfElement;

    public MyHashMap() {
        size = 0;
        currentCapacity = DEFAULT_CAPACITY;
        arrayOfElement = new Node[DEFAULT_CAPACITY];
    }

    public MyHashMap(int userCapacity) {
        if (userCapacity <= 0) {
            throw new RuntimeException("Array size is incorrect");
        }
        arrayOfElement = new Node[userCapacity];
        currentCapacity = userCapacity;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        putIntoArray(key, value);
    }

    private void putIntoArray(K key, V value) {
        if (arrayOfElement[getIndexElementArray(key)] == null) {
            arrayOfElement[getIndexElementArray(key)] = new Node<>(key, value, null);
            size++;
            return;
        } else {
            Node<K, V> existNode = arrayOfElement[getIndexElementArray(key)];
            for (Node<K, V> i = existNode; i != null; i = i.next) {
                if (Objects.equals(i.key, key)) {
                    i.value = value;
                    break;
                } else if (i.next == null) {
                    Node<K, V> newNode = new Node<>(key, value, null);
                    i.next = newNode;
                    size++;
                    break;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        for (Node<K, V> i = arrayOfElement[getIndexElementArray(key)]; i != null; i = i.next) {
            if (Objects.equals(i.key, key)) {
                return i.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexElementArray(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % arrayOfElement.length;
    }

    private void checkSize() {
        if (LOAD_FACTOR <= (double)(size + 1) / currentCapacity) {
            resize();
        }
    }

    private void resize() {
        size = 0;
        int modifiedLength = currentCapacity * 2;
        Node<K, V>[] copyElementNode = arrayOfElement;
        arrayOfElement = new Node[modifiedLength];
        currentCapacity = modifiedLength;
        for (Node<K, V> elementNode : copyElementNode) {
            for (Node<K, V> i = elementNode; i != null; i = i.next) {
                putIntoArray(i.key, i.value);
            }
        }
    }

    private static class Node<K,V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
