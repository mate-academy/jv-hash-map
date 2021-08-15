package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final float loadingFactor = 0.75f;
    private int mapCapacity = 16;
    private int mapSize = 0;

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[mapCapacity];
    }

    public void grow() {
        Node<K, V> [] tableCopy = Arrays.copyOf(table, mapCapacity);
        table = new Node[mapCapacity * 2];
        int oldCapacity = mapCapacity;
        mapCapacity *= 2;
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> e = tableCopy[i];
            if (e != null) {
                put(e.getKey(), e.getValue());
                mapSize--;
                if (e.next != null) {
                    while (e.next != null) {
                        put(e.next.getKey(), e.next.getValue());
                        mapSize--;
                        e = e.next;
                    }
                }
            }
        }
        mapSize--;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            Node<K, V> e = table[0];
            if (table[0] == null && mapSize < mapCapacity * loadingFactor) {
                table[0] = new Node<>(key, value);
            } else if (table[0] != null) {
                e.setValue(value);
                return;
            } else if (mapSize < mapCapacity * loadingFactor) {
                grow();
                put(key, value);
            }
        } else {
            int index = Math.abs(Objects.hash(key) % mapCapacity);
            Node<K, V> e = table[index];
            if (table[index] == null && mapSize < mapCapacity * loadingFactor) {
                table[index] = new Node<>(key, value);
            } else if (table[index] != null && mapSize < mapCapacity * loadingFactor) {
                if (Objects.equals(e.getKey(), key)) {
                    e.setValue(value);
                    return;
                } else {
                    while (e.next != null) {
                        if (Objects.equals(e.getKey(), key)) {
                            e.setValue(value);
                            return;
                        }
                        e = e.next;
                    }
                    e.next = new Node<>(key, value);
                }
            } else if (mapSize == mapCapacity * loadingFactor) {
                grow();
                put(key, value);
            }
        }
        mapSize++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].getValue();
        } else {
            int index = Math.abs(Objects.hash(key) % mapCapacity);
            Node<K, V> e = table[index];
            if (e == null) {
                return null;
            } else if (Objects.equals(e.getKey(), key)) {
                return e.getValue();
            } else {
                while (e != null) {
                    if (Objects.equals(e.getKey(), key)) {
                        return e.getValue();
                    }
                    e = e.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return mapSize;
    }
}
