package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_LENGTH = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int newLength;
    private Node<K, V>[] table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_LENGTH];

    @Override
    public void put(K key, V value) {
        Node<K, V>[] oldTab = table;
        if (size >= (int) (DEFAULT_LOAD_FACTOR * newLength)) {
            oldTab = resize();
        }
        table = addByIndex(key, value, oldTab);
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (key == null && node.key == null || node.key != null && node.key.equals(key)) {
                return node.value;
            }
        }
        return table[index].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V> [] addByIndex(K key, V value, Node<K, V>[] oldTab) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        Node<K, V> currentBucket = oldTab[index];
        if (currentBucket == null) {
            oldTab[index] = newNode;
            size++;
        } else {
            Node<K, V> prevNode = null;
            while (currentBucket != null) {
                if (Objects.equals(currentBucket.key, key)) {
                    currentBucket.value = value;
                    return oldTab;
                }
                prevNode = currentBucket;
                currentBucket = currentBucket.next;
            }
            prevNode.next = newNode;
            size++;
        }
        return oldTab;
    }

    private int getIndex(Object key) {
        int index;
        return (key == null) ? 0 : (index = Math.abs(key.hashCode() % newLength));
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldLength = oldTable.length;
        if (size >= (int) (DEFAULT_LOAD_FACTOR * newLength)) {
            size = 0;
            newLength = oldLength << 1;
            return arrayCopy(oldTable);
        }
        return oldTable;
    }

    private Node<K, V>[] arrayCopy(Node<K, V>[] oldTable) {
        Node<K, V>[] newTab = (Node<K,V>[]) new Node[newLength];
        table = newTab;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> bucket = oldTable[i];
            for (Node<K, V> node = bucket; node != null; node = node.next) {
                addByIndex(node.key, node.value, newTab);
            }
        }
        return newTab;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
