package core.basesyntax;


public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] tabl;
    private int size;

    public MyHashMap() {
        tabl = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K,V> newNode = new Node<>(key, value);
        if (tabl[index] == null) {
            tabl[index] = newNode;
        } else {
            Node<K, V> current = tabl[index];
            while (true) {
                if (key == current.key || (key != null && key.equals(current.key))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        size++;
        if (size >= tabl.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> current = tabl[index];
        while (current != null) {
            if (key == current.key || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % tabl.length;
    }

    private void resize() {
        Node<K, V>[] oldtable = tabl;
        tabl = new Node[oldtable.length * 2];
        size = 0;
        for (Node<K, V> element : oldtable) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }
}
