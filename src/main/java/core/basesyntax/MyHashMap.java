package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCapacity = 16;
    private final float loadFactor = 0.75F;

    private Node<K,V>[] mass;
    private int size;
    private int capacity;

    MyHashMap() {
        mass = new Node[defaultCapacity];
        this.size = 0;
        capacity = defaultCapacity;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (size >= defaultCapacity * loadFactor) {
            newCapacity();
        }
        Node<K, V> collusionNode = mass[index];
        while (collusionNode != null) {
            if (key == collusionNode.key
                    || key != null && key.equals(collusionNode.key)) {
                collusionNode.value = value;
                return;
            }
            collusionNode = collusionNode.next;
        }
        collusionNode = new Node<>(key,value,mass[index]);
        mass[index] = collusionNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        if (index >= capacity || index < 0) {
            newCapacity();
        }
        Node<K,V> collusionNode = mass[index];
        if (collusionNode == null) {
            return null;
        }
        while (collusionNode != null) {
            K testKey = collusionNode.key;
            if ((testKey == key)
                    || (testKey != null && testKey.equals(key))) {
                return collusionNode.value;
            }
            collusionNode = collusionNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % defaultCapacity);
    }

    private void newCapacity() {
        Node<K, V>[]newNode = new Node[2 * capacity];
        for (int i = 0; i < mass.length; i++) {
            newNode[i] = mass[i];
        }
        mass = newNode;
    }

    private class Node<K,V> {
        Node<K, V> next;
        K key;
        V value;

        private Node(K key, V value, Node<K,V> next) {
            this.next = next;
            this.value = value;
            this.key = key;
        }
    }
}
