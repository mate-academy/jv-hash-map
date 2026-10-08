package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int calculatedIndex;
        if (key == null) {
            calculatedIndex = 0;
        } else {
            calculatedIndex = Math.floorMod(key.hashCode(), table.length);
        }
        Node<K,V> current = table[calculatedIndex];
        Node<K,V> previous = null;

        if (table[calculatedIndex] == null) {
            table[calculatedIndex] = new Node<>(key,value);
            size++;
        } else {
            while (current != null) {
                if ((!Objects.equals(current.key, key))) {
                    previous = current;
                    current = current.next;

                } else {
                    current.value = value;
                    return;
                }
                if (current == null) {
                    previous.next = new Node<>(key,value);
                    size++;
                }
            }
        }
        if ((double)size / (double)table.length >= LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int calculatedIndex;
        if (key == null) {
            calculatedIndex = 0;
        } else {
            calculatedIndex = Math.floorMod(key.hashCode(), table.length);
        }
        Node<K,V> current = table[calculatedIndex];
        while (current != null) {
            if (Objects.equals(current.key,key)) {
                return current.value;
            } else {
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int calculatedIndex;
        Node<K,V>[] oldTable = table;

        table = new Node[oldTable.length * 2];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K,V> current = oldTable[i];

            while (current != null) {
                if (current.key == null) {
                    calculatedIndex = 0;
                } else {
                    calculatedIndex = Math.floorMod(current.key.hashCode(), table.length);
                }
                Node<K,V> newCurrent = table[calculatedIndex];

                if (table[calculatedIndex] == null) {
                    table[calculatedIndex] = new Node<>(current.key, current.value);
                } else {
                    while (newCurrent.next != null) {
                        newCurrent = newCurrent.next;
                    }
                    newCurrent.next = new Node<>(current.key, current.value);
                }
                current = current.next;

            }
        }
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key,V value) {
            this.key = key;
            this.value = value;
        }
    }
}
