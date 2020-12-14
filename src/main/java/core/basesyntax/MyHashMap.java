package core.basesyntax;

import java.util.HashMap;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] array;
    private static int capacity = 16;
    private int size = 0;
    private int threshold;

    @Override
    public void put(K key, V value) {
        Node<K, V>[] temp;
        int oldCapacity = array == null ? 0 : array.length;
        int newCapacity, th = 0;
        if (array == null || array.length == 0) {
          temp = resize();
          int
        }

        size++;
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] temp = array;
        int oldCapacity = array == null ? 0 : array.length;
        int newCapacity, th = 0;
        return new Node[size];
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private int getHash(Object key) {
            return key == null ? 0 : key.hashCode() % capacity;
        }
    }
}
/*
Хешмапа
1. Имеет поле Node[] - размером в 16
a) Node имеет поле int hash
2. Внутри метода put, есть метод который считает хеш ключа(правка в Б), который передается
а)   в случае null = 0 || key.hashcode ^ >>>16 (скорее всего просто делиться по остатку на 16)
б) (!) Вычисляет не хеш, а место в массиве куда этот хеш можно закинуть.






 */