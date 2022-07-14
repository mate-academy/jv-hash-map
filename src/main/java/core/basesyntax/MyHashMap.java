package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int PLACE_FOR_NULL_KEY = 0;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        if (++size > threshold) {
            resize();
        }
        insertValue(newNode);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> searchingNode = getNode(key);
        return searchingNode == null ? null : searchingNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> searchingNode;
        searchingNode = table[getIndex(key)];
        if (searchingNode == null) {
            return null;
        }
        while (!Objects.equals(key, searchingNode.key)) {
            if (searchingNode.next == null) {
                return null;
            }
            searchingNode = searchingNode.next;
        }
        return searchingNode;
    }

    private int getIndex(K key) {
        return key == null ? PLACE_FOR_NULL_KEY : Math.abs(key.hashCode()) % table.length;
    }

    private void insertValue(Node<K, V> entry) {
        Node<K, V> replacingNode;
        Node<K, V> nodeBefore = null;
        int index = getIndex(entry.key);
        replacingNode = table[index];
        if (replacingNode == null) {
            table[index] = entry;
            return;
        }
            while (replacingNode != null) {
                if (Objects.equals(entry.key, replacingNode.key)) {
                    replacingNode.value = entry.value;
                    size--;
                    return;
                }
                nodeBefore = replacingNode;
            replacingNode = replacingNode.next;
        }
        nodeBefore.next = entry;
    }

    private void resize() {
        threshold = threshold << 1;
        Node<K, V>[] newTable = new Node[table.length << 1];
        transfer(newTable);
    }

    private void transfer(Node<K, V>[] newTab) {
        Node<K, V>[] oldTab = table;
        table = newTab;
        Node<K, V> current;
        int elemLeft = size - 1;
        for (int i = 0; i < oldTab.length; i++) {
            if (oldTab[i] != null) {
                put(oldTab[i].key,
                        oldTab[i].value);
                size--;
                current = oldTab[i];
                if (current.next != null) {
                    oldTab[i] = current.next;
                    i--;
                }
                elemLeft--;
                if (elemLeft == 0) {
                    return;
                }
            }
        }
    }
}
