package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        grow();
        int keyHash = getHash(key);
        if (table[keyHash % table.length] == null) {
            table[keyHash % table.length] = new Node<>(key, value, keyHash, null);
            size++;
        } else {
            Node<K, V> curNode = table[keyHash % table.length];
            while (curNode != null) {
                if (key == null ? curNode.key == null : key.equals(curNode.key)) {
                    curNode.value = value;
                    return;
                }
                if (curNode.next == null) {
                    curNode.next = new Node<>(key, value, keyHash, null);
                    size++;
                    return;
                }
                curNode = curNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> curNode = table[getHash(key) % table.length];
        while (curNode != null) {
            if (key == null ? curNode.key == null : key.equals(curNode.key)) {
                return curNode.value;
            }
            curNode = curNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        int code;
        return Math.abs(key == null ? 0 : (code = key.hashCode()) ^ (code >>> 16));
    }

    private void grow() {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length << 1];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    static class Node<K, V> {
        private final int hashcode;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, int hashcode, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hashcode = hashcode;
        }
    }
}
