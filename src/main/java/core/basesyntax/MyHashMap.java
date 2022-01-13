package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    static final int INITIAL_THRESHOLD = 12;
    private MyHashMap.Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
        table = new MyHashMap.Node[INITIAL_CAPACITY];
        threshold = INITIAL_THRESHOLD;
    }

    private int hash(Object key) {
        return key == null ? 0 : (key.hashCode() < 0 ? key.hashCode() * -1 : key.hashCode());
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        int index = newNode.hash % table.length;
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
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                list.add(table[i]);
                Node<K, V> currentNode = table[i];
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

    @Override
    public V getValue(K key) {
        int index = hash(key) % table.length;
        if (table[index] != null) {
            if (Objects.equals(table[index].key, key)) {
                return table[index].value;
            } else {
                return getFromChain(key, index);
            }
        }
        return null;
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

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
