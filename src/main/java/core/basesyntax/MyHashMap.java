package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final double RESIZE_BOUND = 0.75;
    private int size;
    private int threshold;
    private Node<K, V>[] nodeArray;

    public MyHashMap() {
        nodeArray = new Node[DEFAULT_SIZE];
        threshold = (int) (nodeArray.length * RESIZE_BOUND);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>((K) key, (V) value, null);
        int index = (key == null) ? 0 : key.hashCode() & (nodeArray.length - 1);
        if (nodeArray[index] == null) {
            nodeArray[index] = newNode;
            size++;
        } else {
            Node current = nodeArray[index];
            while (current != null) {
                if (key == current.key || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : key.hashCode() & (nodeArray.length - 1);
        Node<K, V> current = nodeArray[index];
        while (current != null) {
            if (key == current.key || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] newArray = new Node[nodeArray.length * 2];
        Node<K, V>[] oldArray = nodeArray;
        nodeArray = newArray;
        threshold = (int) (nodeArray.length * RESIZE_BOUND);
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
