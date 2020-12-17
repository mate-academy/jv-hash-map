package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_INDEX = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int currentArraySize;
    private MyNode<K,V> [] arrayBaskets;
    private int size;

    public MyHashMap() {
        currentArraySize = INITIAL_CAPACITY;
        arrayBaskets = new MyNode[currentArraySize];
        size = 0;
    }

    private void put(MyNode<K,V> newNode) {
        int indexBasket = getIndex(newNode.getHashKey());
        if (arrayBaskets[indexBasket] != null) {
            addToList(newNode, indexBasket);
        } else {
            arrayBaskets[indexBasket] = newNode;
        }
        size++;
    }

    @Override
    public void put(K key, V value) {
        MyNode<K, V> newNode = new MyNode<K, V>(key, value);
        if (size > currentArraySize * LOAD_INDEX) {
            resize();
        }
        put(newNode);
    }

    @Override
    public V getValue(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = getIndex(key.hashCode());
        }
        MyNode<K,V> current = arrayBaskets[index];
        while (current != null) {
            if (isKeyEqual(current.getKey(), key)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean isKeyEqual(K key1, K key2) {
        return key1 == null && key2 == null || key1 != null && key1.equals(key2);
    }

    private int getIndex(int hashKye) {
        return Math.abs(hashKye % currentArraySize);
    }

    private void addToList(MyNode<K, V> newNode, int index) {
        MyNode<K, V> currentNode = arrayBaskets[index];
        while (currentNode != null) {
            if (isKeyEqual(currentNode.getKey(), newNode.getKey())) {
                currentNode.setValue(newNode.getValue());
                size--;
                return;
            }
            if (currentNode.getNext() == null) {
                currentNode.setNext(newNode);
                return;
            }
            currentNode = currentNode.getNext();
        }
        currentNode.setNext(newNode);
    }

    private void resize() {
        size = 0;
        currentArraySize = currentArraySize << 1;
        MyNode<K, V> [] memArrayBaskets = arrayBaskets;
        arrayBaskets = new MyNode[currentArraySize];
        for (int i = 0; i < memArrayBaskets.length; i++) {
            if (memArrayBaskets[i] != null) {
                put(new MyNode<K,V>(memArrayBaskets[i].getKey(), memArrayBaskets[i].getValue()));
                MyNode<K,V> current = memArrayBaskets[i].getNext();
                while (current != null) {
                    put(new MyNode<K,V>(current.getKey(), current.getValue()));
                    current = current.getNext();
                }
            }
        }
    }
}

