package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] nodes;
    private int volume;

    public MyHashMap() {
        nodes = new Node[INITIAL_CAPACITY];
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public void put(K key, V value) {
        Node<K, V> currentNode = new Node<>(key, value, null);
        Node<K, V> temporaryNode;
        int index = getKey(key);
        if (volume == threshold()) {
            resize();
        }
        if (nodes[index] == null) {
            nodes[index] = currentNode;
            volume++;
        } else {
            temporaryNode = nodes[index];
            while (temporaryNode != null) {
                if (Objects.equals(temporaryNode.key, key)) {
                    temporaryNode.value = value;
                    break;
                }
                if (temporaryNode.next == null) {
                    temporaryNode.next = currentNode;
                    volume++;
                    break;
                }
                temporaryNode = temporaryNode.next;
            }
        }
    }

    public V getValue(K key) {
        int index;
        Node<K, V> temporaryNode;
        index = getKey(key);
        temporaryNode = nodes[index];
        while (temporaryNode != null) {
            if (Objects.equals(temporaryNode.key, key)) {
                return temporaryNode.value;
            }
            temporaryNode = temporaryNode.next;
        }
        return null;
    }

    public int getSize() {
        return volume;
    }

    private void resize() {
        Node<K, V>[] temporaryArray = nodes;
        nodes = new Node[nodes.length * 2];
        volume = 0;
        for (Node<K, V> node : temporaryArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int threshold() {
        return (int) (nodes.length * LOAD_FACTOR);
    }

    private int getKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % nodes.length);
    }

}
