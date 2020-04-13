package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTORY = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTORY * table.length) {
            addCapacity();
        }
        if (table[hashKey(key)] == null) {
            table[hashKey(key)] = new Node<>(key, value, null);
            size++;
        } else {
            Node nodeV = arraySearch(key);
            if (nodeV == null) {
                return;
            }
            if (nodeV.next == null && ((key == null || nodeV.key == null)
                    ? nodeV.key != key : !nodeV.key.equals(key))) {
                nodeV.next = new Node(key, value, null);
                size++;
            } else {
                nodeV.value = value;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (arraySearch(key) != null) {
            return arraySearch(key).value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashKey(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() % (table.length - 1) > 0
                ? key.hashCode() % (table.length - 1)
                : key.hashCode() % (table.length - 1) * -1;
    }

    private void addCapacity() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private Node<K, V> arraySearch(K key) {
        Node<K, V> nodeA = table[hashKey(key)];
        if (nodeA != null) {
            while (nodeA.next != null) {
                if ((key == null || nodeA.key == null)
                        ? nodeA.key == key : nodeA.key.equals(key)) {
                    return nodeA;
                }
                nodeA = nodeA.next;
            }
        }
        return nodeA;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
