package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCapacity = 16;
    private final float loadFactor = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[defaultCapacity];
        size = 0;
        threshold = (int) (defaultCapacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 >= threshold) {
            resize();
        }
        int hash = hash(key);
        int index = hash & (table.length - 1);
        if (key == null) {
            if (table[index] == null) {
                table[index] = new Node<>(hash,key,value,null);
                size++;
                return;
            } else {
                Node<K,V> currentNode = table[index];
                while (currentNode.next != null) {
                    if (currentNode.key == null) {
                        currentNode.value = value;
                        return;
                    }
                    currentNode = currentNode.next;
                }
                if (currentNode.key == null) {
                    currentNode.value = value;
                    return;
                } else {
                    currentNode.next = new Node<>(hash, key, value, null);
                    size++;
                    return;
                }
            }
        }
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> currentNode = table[index];
            if (currentNode.key != null && currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            while (currentNode != null) {
                if (currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash,key,value,null);
                    break;
                }
                currentNode = currentNode.next;
            }

        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = hash & (table.length - 1);
        if (table[index] == null) {
            return null;
        }
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == null ? key == null : currentNode.key.equals(key)) {
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

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = node.hash & (newCapacity - 1);

                Node<K, V> nextNode = node.next;

                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = nextNode;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }
}
