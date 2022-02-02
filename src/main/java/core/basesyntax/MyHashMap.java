package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int capacity = DEFAULT_CAPACITY;
    private int treshold = (int)(capacity * LOAD_FACTOR);
    private Node<K,V>[] table = new Node[capacity];

    @Override //improving
    public void put(K key, V value) {
        if (size + 1 > treshold) {
            resize();
        } //To check if the element already exists
        Node<K,V> element = table[getIndexFromHash(key)];
        if (element != null && (element.getValue().equals(value)
                || element.getValue() == value)) {
            return;
        }
        if (element != null && element.getNext() != null) {
            while (element != null) {
                if (element.getValue() == value || element.getValue().equals(value)) {
                    return;
                }
                element = element.getNext();
            }
        }
        if (table[getIndexFromHash(key)] != null) {
            element = table[getIndexFromHash(key)];
            if (element.getNext() == null) {
                if (element.getKey() == key) {
                    element.setValue(value);
                    return;
                }
                element.setNext(new Node<>(getHash(key),key,value,null));
                size++;
                return;
            }
            while (element.getNext() != null) {
                if (element.getKey() == key) {
                    element.setValue(value);
                    return;
                }
                element = element.getNext();
            }
            element.setNext(new Node<>(getHash(key),key,value,null));
            size++;
            return;
        }

        table[getIndexFromHash(key)] = new Node<>(getHash(key),key,value,null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexFromHash(key);
        Node<K, V> returnNode = table[index];
        while (returnNode != null) {
            if (Objects.equals(returnNode.getKey(), key)) {
                return returnNode.getValue();
            }
            returnNode = returnNode.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        int h = (key.hashCode() * 3) * (key.hashCode() * 5) / 17;
        if (h < 0) {
            h = h * -1;
            return h;
        }
        return h;
    }

    private int getIndexFromHash(K key) {
        return key == null ? 0 : Math.abs(getHash(key) % table.length);
    }

    private void resize() {
        capacity = capacity * 2;
        treshold = (int)(capacity * LOAD_FACTOR);
        Node[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node element : oldTable) {
            while (element != null) {
                put((K) element.getKey(), (V) element.getValue());
                element = element.getNext();
            }
        }
    }
}
