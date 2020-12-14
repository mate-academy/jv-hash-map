package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] map;
    private int size;
    private double threshold;

    public MyHashMap() {
        this.map = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        this.threshold = INITIAL_CAPACITY * LOAD_FACTOR;
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = newNode.hash % map.length;
        if (map[index] == null) {
            map[index] = newNode;
            size++;
        } else {
            Node<K, V> iterationNode = map[index];
            while (iterationNode.next != null || isSameKey(iterationNode, key)) {
                if (isSameKey(iterationNode, key)) {
                    iterationNode.value = newNode.value;
                    return;
                }
                iterationNode = iterationNode.next;
            }
            iterationNode.next = newNode;
            size++;

        }
    }

    private boolean isSameKey(Node<K, V> iterationNode, K key) {
        return (key == null && iterationNode.key == null)
                || (iterationNode.key != null && iterationNode.key.equals(key));
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = getHashCode(key) % map.length;
        Node<K, V> iterationNode = map[index];
        while (iterationNode != null) {
            if (isSameKey(iterationNode, key)) {
                return iterationNode.value;
            }
            iterationNode = iterationNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldMap = map;
        map = (Node<K, V>[]) new Node[oldMap.length * CAPACITY_MULTIPLIER];
        size = 0;
        threshold *= CAPACITY_MULTIPLIER;
        for (Node<K, V> node : oldMap) {
            if (node != null) {
                this.put(node.key, node.value);
                while (node.next != null) {
                    this.put(node.next.key, node.next.value);
                    node = node.next;
                }
            }
        }
    }

    private static <K> int getHashCode(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.hash = MyHashMap.getHashCode(key);
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
