package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] objects;

    public MyHashMap() {
        size = 0;
        objects = new Node[DEFAULT_CAPACITY];
    }

    private void resize() {
        if (size >= objects.length * LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] temporary = objects;
            objects = new Node[objects.length * 2];
            for (int i = 0; i < temporary.length; i++) {
                if (temporary[i] != null) {
                    Node<K, V> moveNode = temporary[i];
                    while (moveNode != null) {
                        put(moveNode.key, moveNode.value);
                        moveNode = moveNode.next;
                    }
                }
            }
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        h = h ^ (h >>> 16);
        if (h < 0) {
            h = -1 * h + 1;
        }
        return (key == null) ? 0 : h;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        K key = (K) obj;

        return this.equals(obj);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> nodeToAdd = new Node<>(key, value);
        nodeToAdd.hash = hash(key);
        int bucket = nodeToAdd.hash % objects.length;
        if (objects[bucket] == null) {
            objects[bucket] = nodeToAdd;
            nodeToAdd.next = null;
            size++;
        } else {
            Node<K, V> inLinkObject = objects[bucket];
            int count = 0;
            while (inLinkObject != null) {
                if (nodeToAdd.key == inLinkObject.key
                        || nodeToAdd.key.equals(inLinkObject.key)) {
                    inLinkObject.value = value;
                    count++;
                    break;
                }
                inLinkObject = inLinkObject.next;
            }
            if (count == 0) {
                inLinkObject = objects[bucket];
                while (inLinkObject.next != null) {
                    inLinkObject = inLinkObject.next;
                }
                inLinkObject.next = nodeToAdd;
                nodeToAdd.next = null;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = objects[hash(key) % objects.length];
        while (node != null) {
            if (node.key == key || key.equals(node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private int hash;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }
}
