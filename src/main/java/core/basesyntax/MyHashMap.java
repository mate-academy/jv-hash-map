package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> oldNode = table[getIndex(key)];
        if (oldNode == null) {
            table[getIndex(key)] = newNode;
            size++;
            resize();
        } else {
            while (oldNode != null) {
                if (oldNode.key == newNode.key || oldNode.key != null
                        && oldNode.key.equals(newNode.key)) {
                    oldNode.value = newNode.value;
                    break;
                } else if (oldNode.next == null) {
                    oldNode.next = newNode;
                    size++;
                    break;
                }
                oldNode = oldNode.next;
            }
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
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

    private int getIndex(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode() % table.length);
    }

    private void resize() {
        if (size > table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;;
            size = 0;
            table = new Node[oldTable.length * 2];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
