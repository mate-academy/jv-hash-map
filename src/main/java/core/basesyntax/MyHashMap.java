package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_THE_LENGTH = 2;
    private Node<K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int currentLength = table.length;
    private int size;

    @Override
    public void put(K key, V value) {
        resizeIfNeed();
        int index = getIndex(key);
        Node<K, V> node = new Node<>(index, key, value, null);
        Node<K, V> currentNode;
        index = index % currentLength;
        currentNode = table[index];
        if (node.hash == 0) {
            setInNullBucket(node);
            return;
        }
        if (table[index] != null) {
            if (!table[index].key.equals(node.key)) {
                while (true) {
                    if (currentNode.next == null) {
                        currentNode.next = node;
                        size++;
                        return;
                    }
                    currentNode = currentNode.next;
                }
            } else {
                table[index].value = node.value;
                return;
            }
        } else {
            table[index] = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
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

    private void resizeIfNeed() {
        if (size == currentLength * DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] oldTab = table;
            table = new Node[currentLength *= INCREASE_THE_LENGTH];
            size = 0;
            for (Node<K,V> current : oldTab) {
                if (current != null) {
                    while (current != null) {
                        put(current.key, current.value);
                        current = current.next;
                    }
                }
            }
        }
    }

    private int getIndex(K key) {
        int index = Node.hashCode(key);
        index = (index >= 1 ? index : index * -1) % currentLength;
        return index;
    }

    private void setInNullBucket(Node<K,V> node) {
        if (table[0] == null) {
            table[0] = node;
            size++;
        } else {
            table[0] = node;
        }
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public static int hashCode(Object key) {
            return (key == null) ? 0 : key.hashCode() * 17 + 1;
        }

        public final String toString() {
            return key + "=" + value;
        }
    }
}
