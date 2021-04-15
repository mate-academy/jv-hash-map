package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float loadFactor = 0.75f;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int size;
    private int threshold = (int) (table.length * loadFactor);

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = indexFor(Objects.hashCode(key));
        Node<K, V> currentNode = table[index];
        Node<K, V> newNod = new Node<>(Objects.hashCode(key), key, value, null);
        if (table[index] == null) {
            table[index] = newNod;
            size++;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNod;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }

    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = table[indexFor(Objects.hashCode(key))];
        while (getNode != null) {
            if (Objects.equals(getNode.key, key)) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexFor(int hashCode) {
        return hashCode == 0 ? 0 : Math.abs(hashCode & (table.length - 1));
    }

    private void resize() {
        size = 0;
        Node<K, V>[] nodeCopyTable = table;
        table = new Node[table.length << 1];
        threshold = (int) (table.length * loadFactor);
        transfer(nodeCopyTable);

    }

    private void transfer(Node<K, V>[] nodeCopyTable) {
        for (Node<K, V> node : nodeCopyTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public static class Node<K, V> {
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
