package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] array;
    private int size;

    public MyHashMap() {
        array = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * array.length) {
            resize();
        }
        int index = hashCodeIndex(key);
        Node<K, V> node = new Node<>(key, value, null);
        if (array[index] != null) {
            putToList(array[index], node);
            return;
        }
        array[index] = node;
        size++;
    }

    private void putToList(Node<K, V> oldNode, Node<K, V> newNode) {
        if (checkNodeKey(oldNode.key, newNode.key)) {
            oldNode.value = newNode.value;
            return;
        }
        while (oldNode.next != null) {
            if (checkNodeKey(oldNode.key, newNode.key)) {
                oldNode.value = newNode.value;
                return;
            }
            oldNode = oldNode.next;
        }
        oldNode.next = newNode;
        size++;
    }

    private boolean checkNodeKey(K currentKey, K key) {
        return Objects.equals(currentKey,key);
    }

    @Override
    public V getValue(K key) {
        int index = hashCodeIndex(key);
        Node<K, V> node = array[index];
        while (node != null) {
            if (checkNodeKey(node.key, key)) {
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

    private int hashCodeIndex(K key) {
        return (key == null) ? 0 : (key.hashCode() & (array.length - 1));
    }

    private void resize() {
        Node<K, V>[] oldArray = array;
        array = new Node[array.length * 2];
        size = 0;
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
