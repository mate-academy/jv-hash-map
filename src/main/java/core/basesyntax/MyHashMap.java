package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int initialCapacity;
    private int size;
    private Node[] nodes;

    MyHashMap() {
        initialCapacity = DEFAULT_CAPACITY;
        size = 0;
        nodes = new Node[initialCapacity];
    }

    private int findIndex(int hash) {
        return hash % nodes.length;
    }

    private int getHash(int hashCode) {
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return Math.abs(hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4));

    }

    @Override
    public void put(K key, V value) {
        if (size >= nodes.length * LOAD_FACTOR) {
            resize();
        }
        boolean counter = true;
        if (key == null) {
            if (nodes[0] == null) {
                nodes[0] = new Node(key, value, null);
                size++;
            } else {
                nodes[0].values = value;
            }
            return;
        }
        int index = findIndex(getHash(key.hashCode()));
        if (nodes[index] == null) {
            nodes[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> checkNode = nodes[index];
            while (checkNode != null) {
                if (key.equals(checkNode.key)) {
                    checkNode.values = value;
                    counter = false;
                    break;
                }
                checkNode = checkNode.next;
            }
            if (counter) {
                Node<K, V> newEntry = new Node<>(key, value, nodes[index]);
                nodes[index] = newEntry;
                size++;
            }
        }
    }

    private void resize() {
        if (initialCapacity <= Integer.MAX_VALUE / 2) {
            size = 0;
            Node[] newTable = nodes;
            nodes = new Node[nodes.length * 2];
            for (Node<K, V> kvNode : newTable) {
                Node<K, V> node = kvNode;
                while (node != null) {
                    put(node.key, node.values);
                    node = node.next;
                }
            }
            initialCapacity *= 2;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return (V) nodes[0].values;
        }
        int index = findIndex(getHash(key.hashCode()));
        Node<K, V> outNode = nodes[index];
        while (outNode != null) {
            if (key.equals(outNode.key)) {
                return outNode.values;
            }
            outNode = outNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private static class Node<K, V> {
        private K key;
        private V values;
        private Node next;

        private Node(K key, V values, Node next) {
            this.key = key;
            this.values = values;
            this.next = next;
        }

    }
}