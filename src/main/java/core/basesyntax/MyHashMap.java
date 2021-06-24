package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int sizeMap = 0;
    private int capacity = DEFAULT_CAPACITY;
    private Node[] nodes = new Node[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (sizeMap >= (capacity * LOAD_FACTOR)) {
            resize();
        }
        if (getValue(key) != null) {
            setValue(key, value);
            return;
        }
        int position = position(getIndex(key));
        if (nodes[position] == null) {                          //якщо ячейка вільна то ставим у ячейку ноду.
            nodes[position] = new Node(key, value, null);
        } else {                                                //якщо зайнята то ноду у ячейць вказуем як наступну.
            nodes[position] = new Node(key, value, nodes[position]);
        }
        sizeMap++;
    }

    @Override
    public V getValue(K key) {

        Node result = nodes[position(getIndex(key))];
        while (result != null) {
            if (key == result.key || result.key != null && result.key.equals(key)) {
                return (V) result.value;
            }
            result = result.next;
        }
        return null;
    }

    private void setValue(K key, V value) {
        Node result = nodes[position(getIndex(key))];
        while (result != null) {
            if (result.key == key || result.key.equals(key)) {
                result.value = value;
                return;
            }
            result = result.next;
        }
    }

    private int position(int hash) {
        return Math.abs(hash) % capacity;
    }

    private void resize() {
        int oldCapacity = capacity;
        capacity = oldCapacity * 2;
        Node[] newNodes = new Node[oldCapacity * 2];
        Node[] oldNodes = nodes;
        nodes = newNodes;
        sizeMap = 0;
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

    private int getIndex(K key) {
        return (key == null ? 0 : key).hashCode();
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private MyHashMap.Node<K, V> next;

        Node(K key, V value, MyHashMap.Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
