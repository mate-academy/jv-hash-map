package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private int threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    private int size;
    private MyNode<K, V>[] table;

    public MyHashMap() {
        table = new MyNode[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getIndex(key);
        MyNode<K, V> inputNode = new MyNode<>(null, key, value);
        MyNode<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = inputNode;
        } else {
            while (currentNode != null) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = inputNode;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        MyNode<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : (key.hashCode() & (table.length - 1));
    }

    private void resizeIfNeeded() {
        if (size >= threshold) {
            size = 0;
            MyNode<K, V>[] oldTable = table;
            MyNode<K, V>[] newTable = new MyNode[table.length * GROW_FACTOR];
            threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
            table = newTable;
            transfer(oldTable);
        }
    }

    private void transfer(MyNode<K, V>[] oldTable) {
        for (MyNode<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class MyNode<K, V> {
        private MyNode<K, V> next;
        private final K key;
        private V value;

        public MyNode(MyNode<K, V> next, K key, V value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
