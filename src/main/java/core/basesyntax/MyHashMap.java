package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    public class Node<K, V> {
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

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private boolean isResized = false;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);

        if (size >= threshold) {
            resize();
        }

        int index = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
        Node<K, V> nodeToPut;
        if (index == 0) {
            nodeToPut = new Node<>(index, key, value, null);
        } else {
            nodeToPut = new Node<>(key.hashCode(), key, value, null);
        }

        if (table[index] == null) {
            table[index] = nodeToPut;
            size++;
            return;
        }

        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }

            if (currentNode.next == null) {
                currentNode.next = nodeToPut;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }

    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (obj == this) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        MyHashMap<K, V> other = (MyHashMap<K, V>) obj;

        if (size != other.size) {
            return false;
        }

        for (Node<K, V> node : table) {
            while (node != null) {
                Object otherValue = other.getValue(node.key);
                if (!Objects.equals(node.value, otherValue)) {
                    return false;
                }
                node = node.next;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Node<K, V> node : table) {
            while (node != null) {
                hash += Objects.hashCode(node.key) ^ Objects.hashCode(node.value);
                node = node.next;
            }
        }
        return hash;
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * 2];

        for (Node<K, V> node : table) {
            while (node != null) {
                int index = (node.key == null) ? 0
                        : Math.abs(node.key.hashCode() % newTable.length);

                Node<K, V> nextNode = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = nextNode;
            }
        }

        table = newTable;
        threshold = (int) (DEFAULT_LOAD_FACTOR * newTable.length);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("{");
        for (Node<K, V> node : table) {
            Node<K, V> current = node;
            while (current != null) {
                str.append(current.key).append("=").append(current.value).append(", ");
                current = current.next;
            }
        }
        if (str.length() > 1) {
            str.setLength(str.length() - 2);
        }
        str.append("}");
        return str.toString();
    }
}
