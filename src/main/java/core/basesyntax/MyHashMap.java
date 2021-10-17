package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    private int size = 0;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = node;
            size++;
        } else {
            for (Node<K,V> indexNode = table[getIndex(key)];
                    indexNode != null; indexNode = indexNode.next) {
                if (isEqual(key, indexNode.key)) {
                    indexNode.value = value;
                    return;
                }
                if (indexNode.next == null) {
                    indexNode.next = node;
                    size++;
                    return;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> indexNode = table[getIndex(key)];
                indexNode != null; indexNode = indexNode.next) {
            if (isEqual(key, indexNode.key)) {
                return indexNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean isEqual(Object firstObject, Object secondObject) {
        return firstObject == secondObject
                || firstObject != null && firstObject.equals(secondObject);
    }

    private void resize() {
        threshold = threshold << 1;
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        for (Node<K, V> node : oldTable) {
            for (; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }
}

