package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int INITIAL_THRESHOLD = 12;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
        table = new Node[INITIAL_CAPACITY];
        threshold = INITIAL_THRESHOLD;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = calculateIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else if (Objects.equals(key, table[index].key)) {
            table[index].value = value;
        } else {
            putInChain(newNode, index);
        }
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        if (table[index] != null) {
            if (Objects.equals(table[index].key, key)) {
                return table[index].value;
            } else {
                return getFromChain(key, index);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putInChain(Node<K, V> newNode, int index) {
        Node<K, V> currentNode = this.table[index];
        while (true) {
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                break;
            } else {
                if (Objects.equals(newNode.key, currentNode.next.key)) {
                    currentNode.next.value = newNode.value;
                    break;
                } else {
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * loadFactor);
        List<Node<K, V>> list = entryList();
        size = 0;
        table = new Node[newCapacity];
        for (Node<K, V> entry: list) {
            put(entry.key, entry.value);
        }
    }

    private List<Node<K, V>> entryList() {
        List<Node<K, V>> list = new ArrayList<>();
        for (Node<K, V> entry: table) {
            if (entry != null) {
                list.add(entry);
                Node<K, V> currentNode = entry;
                while (true) {
                    if (currentNode.next != null) {
                        list.add(currentNode.next);
                        currentNode = currentNode.next;
                    } else {
                        break;
                    }
                }
            }
        }
        return list;
    }

    private V getFromChain(K key, int index) {
        Node<K, V> currentNode = table[index];
        V value;
        while (true) {
            if (Objects.equals(key, currentNode.next.key)) { //
                value = currentNode.next.value;
                break;
            } else {
                currentNode = currentNode.next;
            }
        }
        return value;
    }

    private int calculateIndex(K key) {
        return key == null ? 0
                : (key.hashCode() < 0
                ? (key.hashCode() * -1) % table.length
                : key.hashCode() % table.length);
    }

    protected static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
