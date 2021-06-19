package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_SIZE = 0;
    private static final double CONDITION_RESIZE = 0.75;
    private int sizeMap = DEFAULT_SIZE;
    private int capacity = DEFAULT_CAPACITY;
    private Node[] nodes = new Node[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (sizeMap >= (capacity * CONDITION_RESIZE)) {
            reSize();
        }
        if (getValue(key) != null) {
            setValue(key, value);
            return;
        }
        int position = position((key == null ? 0 : key).hashCode());
        if (nodes[position] == null) {
            nodes[position] = new Node(key, value, null);
            sizeMap++;
        } else {
            nodes[position] = new Node(key, value, nodes[position]);
            sizeMap++;
        }

    }

    @Override
    public V getValue(K key) {

        Node result = nodes[position((key == null ? 0 : key).hashCode())];
        while (result != null) {
            if (key == result.key || result.key != null && result.key.equals(key)) {
                return (V) result.value;
            } else {
                result = result.next;
            }
        }
        return null;
    }

    private void setValue(K key, V value) {
        Node result = nodes[position((key == null ? 0 : key).hashCode())];
        while (result != null) {
            if (result.key == key || result.key.equals(key)) {
                result.value = value;
                return;
            } else {
                result = result.next;
            }
        }
    }

    private int position(int hash) {
        return Math.abs(hash) % capacity;
    }

    private void reSize() {
        int oldCapacity = capacity;
        capacity = oldCapacity * 2;
        Node[] newNodes = new Node[oldCapacity * 2];
        Node[] oldNodes = nodes;
        nodes = newNodes;
        sizeMap = DEFAULT_SIZE;
        for (int i = 0; i < oldCapacity; i++) {
            if (oldNodes[i] != null) {
                put((K) oldNodes[i].key, (V) oldNodes[i].value);
                while (oldNodes[i].next != null) {
                    put((K) oldNodes[i].next.key, (V) oldNodes[i].next.value);
                    oldNodes[i] = oldNodes[i].next;
                }
            }
        }
    }

    @Override
    public int getSize() {
        return sizeMap;
    }

    private static class Node<K, V> {
        private V value;
        private K key;
        private MyHashMap.Node<K, V> next;

        Node(K key, V value, MyHashMap.Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
