package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    int size = 0;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Object[] elementArray;

    MyHashMap() {
        elementArray = new Object[16];
    }

    MyHashMap(int capacity) {
        elementArray = new Object[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (++size > elementArray.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = hash(key);

        if (index > -1) {
            Node currentNode = (Node) elementArray[index];
            if (currentNode == null) {
                elementArray[index] = new Node(key, value);
            } else {
                while (currentNode.next != null) {
                    if (key.equals(currentNode.key)) {
                        currentNode.data = value;
                        size--;
                        return;
                    }
                    currentNode = currentNode.next;
                }
                if (key.equals(currentNode.key)) {
                    currentNode.data = value;
                    size--;
                    return;
                }
                currentNode.next = new Node(key, value);
            }

        } else {
            Node currentNode = (Node) elementArray[0];
            Node tmp = new Node(null, value);
            if (currentNode != null) {
                if (currentNode.key == null) {
                    currentNode.data = value;
                    size--;
                    return;
                }
                tmp.next = currentNode;
                elementArray[0] = tmp;
            } else {
                elementArray[0] = tmp;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node currentNode = (Node) elementArray[0];
        if (index != -1 && size > 0) {
            currentNode = (Node) elementArray[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    break;
                }
                currentNode = currentNode.next;
            }

        }
        return currentNode == null ? null : (V) currentNode.data;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Object[] newArray = Arrays.copyOf(elementArray, elementArray.length);
        elementArray = new Object[size * (size >>> 1)];
        size = 1;
        for (Object obj : newArray) {
            Node element = (Node) obj;
            while (element != null) {
                put((K) element.key, (V) element.data);
                element = element.next;
            }
        }
    }

    private int hash(Object key) {
        if (key != null) {
            return Math.abs(key.hashCode() % elementArray.length);
        }
        return -1;
    }

    private class Node<K, V> {
        K key;
        V data;
        public Node next;

        Node(K key, V data) {
            this.key = key;
            this.data = data;
        }
    }
}

