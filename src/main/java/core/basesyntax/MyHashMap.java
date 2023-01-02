package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[16];
    }

    @Override
    public void put(K key, V value) {
        checkThreshold();
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        for ( ;node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value,null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private void checkThreshold() {
        if (size > (int) (0.75f * table.length)) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getHashcode(K key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode()) ^ (key.hashCode() >>> 16); /*We need this impl due to possibility
        of numbers of bucket collisions if we use default hash method*/
    }

    private int getIndex(K key) {
        return Math.abs(getHashcode(key) % table.length);
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
}
