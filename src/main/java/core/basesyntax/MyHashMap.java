package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] map;
    private int currentCapacity;
    private int threshold;

    @Override
    public void put(K key, V value) {
        updateThreshold();
        if (size >= threshold) {
            resize();
        }
        int index = generateIndex(key);
        if (map[index] == null) {
            map[index] = new Node<>(key, value);
            size++;
        }
        if (index != 0) {
            Node<K,V> tempNode = findInsertNode(map[index], key);
            if (tempNode.next == null && !tempNode.key.equals(key)) {
                tempNode.next = new Node<>(key, value);
                size++;
            } else {
                tempNode.value = value;
            }
        } else {
            map[index].value = value;
        }
    }

    @Override
    public V getValue(K key) {
        if (map == null) {
            return null;
        }
        int index = generateIndex(key);
        Node<K,V> tempNode = map[index];
        if (index == 0) {
            return map[index].value;
        }
        if (tempNode.next == null && map[index].key.equals(key)) {
            return map[index].value;
        } else {
            while (tempNode != null) {
                if (tempNode.key.equals(key)) {
                    return tempNode.value;
                }
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
        private V value;
        private K key;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.value = value;
            this.key = key;
        }
    }

    private int generateIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % currentCapacity + 1;
        }
    }

    private Node<K,V> findInsertNode(Node<K,V> node, K key) {
        Node<K,V> result;
        result = node;
        while (result.next != null) {
            if (result.key.equals(key)) {
                return result;
            }
            result = result.next;
        }
        return result;
    }

    private void updateThreshold() {
        this.threshold = (int) (LOAD_FACTOR * currentCapacity);
    }

    private void resize() {
        if (map == null) {
            map = new Node[INITIAL_CAPACITY];
            currentCapacity = map.length;
        } else {
            size = 0;
            Node<K,V>[] oldMap = map;
            map = new Node[currentCapacity << 1];
            currentCapacity = map.length;
            for (Node<K,V> node: oldMap) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
