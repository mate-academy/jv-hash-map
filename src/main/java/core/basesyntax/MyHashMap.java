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
    private int size = 0;
    private int currentCapacity;
    private Node<K, V>[] hashMapList;

    public MyHashMap() {
        currentCapacity = DEFAULT_CAPACITY;
        hashMapList = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getHash(key);
        Node<K, V> newElement = new Node<>(key, value);
        if (hashMapList[index] != null) {
            Node<K, V> element = hashMapList[index];
            Node<K, V> prevElement = element;
            while (element != null) {
                if (Objects.equals(key, element.key)) {
                    element.value = value;
                    return;
                }
                prevElement = element;
                element = element.nextItem;
            }
            prevElement.nextItem = newElement;
        } else {
            hashMapList[index] = newElement;
        }
        size++;
        checkSize();
    }

    @Override
    public V getValue(K key) {
        int index = getHash(key);
        if (size != 0) {
            Node<K, V> element = hashMapList[index];
            do {
                if (Objects.equals(key, element.key)) {
                    return element.value;
                }
                element = element.nextItem;
            } while (element != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addElement(Node<K, V>[] list, Node<K, V> node) {
        int index = getHash(node.key);
        if (list[index] != null) {
            Node<K, V> element = list[index];
            while (element.nextItem != null) {
                element = element.nextItem;
            }
            element.nextItem = node;
        } else {
            list[index] = node;
        }
    }

    private void checkSize() {
        if (currentCapacity * LOAD_FACTOR < size) {
            currentCapacity = currentCapacity * 2;
            Node<K, V>[] newHashMapList = new Node[currentCapacity];
            for (int i = 0; i < hashMapList.length; i++) {
                if (hashMapList[i] != null) {
                    Node<K, V> movingElement = hashMapList[i];
                    do {
                        if (movingElement.nextItem != null) {
                            Node<K, V> nextElement = movingElement.nextItem;
                            addElement(newHashMapList, movingElement);
                            movingElement.nextItem = null;
                            movingElement = nextElement;
                        } else {
                            addElement(newHashMapList, movingElement);
                            break;
                        }
                    } while (true);
                }
            }
            hashMapList = newHashMapList;
        }
    }

    private int getHash(K key) {
        if (key != null) {
            return Math.abs(key.hashCode()) % currentCapacity;
        }
        return 0;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextItem;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
