package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int position = getPosition(key, table.length);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[position];

        if (table[position] == null) {
            table[position] = newNode;
        } else {
            while (currentNode != null) {
                if (currentNode.key == null) {
                    if (key == null) {
                        currentNode.value = value;
                        return;
                    }
                    if (currentNode.next == null) {
                        currentNode.next = newNode;
                        break;
                    }
                    currentNode = currentNode.next;
                    continue;
                }
                if (currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int position = getPosition(key, table.length);
        Node<K,V> currentNode = table[position];

        if (currentNode == null) {
            return null;
        }
        while (currentNode != null) {
            if (currentNode.key == null || key == null) {
                if (currentNode.key == null && key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
                continue;
            }
            if (currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    //    @Override
    //    public String toString() {
    //        return "MyHashMap{"
    //                +
    //                "size=" + size
    //                +
    //                ", table="
    //                + Arrays.toString(table)
    //                +
    //                '}';
    //    }

    private int getPosition(Object key, int capacity) {
        int position = (key == null) ? 0 : (key.hashCode() % capacity);
        return (position < 0) ? - position : position;
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        //        @Override
        //        public String toString() {
        //            return "N{"
        //                      +
        //                    " k="
        //                    + key
        //                    +
        //                    ", v="
        //                    + value
        //                    +
        //                    ", n="
        //                    + next
        //                    +
        //                    '}';
        //        }
    }
}
