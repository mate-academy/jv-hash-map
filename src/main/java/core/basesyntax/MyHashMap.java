package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        checkTableSize(size);
        int index = findIndex(key);
        Node<K, V> node = new Node<>(key, value, null);
        Node<K, V> tempNode = table[index];
        if (tempNode == null) {
            table[index] = node;
            size++;
        } else {
            while (tempNode != null) {
                if (key == null && tempNode.key == null
                        || key == tempNode.key
                        || key != null && key.equals(tempNode.key)) {
                    tempNode.value = value;
                    break;
                } else if (tempNode.next == null) {
                    tempNode.next = node;
                    size++;
                    break;
                }
                tempNode = tempNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tempNode = table[findIndex(key)];
        while (tempNode != null) {
            if (key == tempNode.key || key != null && key.equals(tempNode.key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int findIndex(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }

    private void checkTableSize(int size) {
        if (size >= (int) (table.length * LOAD_FACTOR)) {
            resize();
        }
    }
}
