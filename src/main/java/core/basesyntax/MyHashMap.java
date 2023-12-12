package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int DEFAULT_CAPACITY = 16;
    private final int GROW_FACTOR = 2;
    private final Double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] innerArray = new Node[DEFAULT_CAPACITY];
    @Override
    public void put(K key, V value) {
        int threshold = (int) (innerArray.length * LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }
        int index = hash(key);
        Node<K, V> currentNode = innerArray[index];
            while (currentNode != null) {
                if (equalsKeys(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (innerArray[index] == null) {
                innerArray[index] = new Node<>(key, value);
            } else {
                currentNode = innerArray[index];
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
                currentNode.next = new Node<>(key, value);
            }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexOfInputKey = hash(key);
        Node<K, V> foundNode = innerArray[indexOfInputKey];
        while (foundNode != null) {
            if (equalsKeys(foundNode.key, key)) {
                return foundNode.value;
            }
            foundNode = foundNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hash(K key) {
        if (key == null) {
            return 0;
        }
        int moduleOfHashKey = Math.abs(key.hashCode());
      return moduleOfHashKey % innerArray.length;
    }

    public boolean equalsKeys(Object k1, Object k2) {
        return (k1 == k2 || k1 != null && k1.equals(k2));
    }

    private void resize() {
        int updatingCapacity = innerArray.length * GROW_FACTOR;
        Node<K, V>[] updateInnerArray = new Node[updatingCapacity];
        for (Node<K, V> item : innerArray) {
            while (item != null) {
                Node<K, V> next = item.next;
                int newIndex = hash(item.key) % updatingCapacity;
                if (updateInnerArray[newIndex] == null) {
                    updateInnerArray[newIndex] = new Node<>(item.key, item.value);
                } else {
                Node<K, V> currentNode = updateInnerArray[newIndex];
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
                currentNode.next = new Node<>(item.key, item.value);
                }
                item = next;
            }
        }
        innerArray = updateInnerArray;
    }

    private static class Node<K, V> {

        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
