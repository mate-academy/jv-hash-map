package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int DEFAULT_ARRAY_LENGTH = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int elementsAmount;
    private Node<K,V> [] valuesArray;
    private int arrayIndex;

    @Override
    public void put(K key, V value) {
        if (elementsAmount == 0) {
            valuesArray = new Node[DEFAULT_ARRAY_LENGTH];
        }
        if (elementsAmount != 0 && elementsAmount > (valuesArray.length * LOAD_FACTOR)) {
            resize();
        }
        arrayIndex = (key == null ? 0 : Math.abs(key.hashCode() % valuesArray.length));
        if (valuesArray[arrayIndex] == null) {
            Node<K, V> newNode = new Node<>(key, value, null);
            valuesArray[arrayIndex] = newNode;
            elementsAmount++;
        } else {
            Node<K,V> currentNode = valuesArray[arrayIndex];
            while (true) {
                if (currentNode.nodeKey != null && currentNode.nodeKey.equals(key)
                        || currentNode.nodeKey == null && key == null) {
                    currentNode.nodeValue = value;
                    break;
                } else {
                    if (currentNode.next == null) {
                        currentNode.next = new Node<>(key,value,null);
                        elementsAmount++;
                        break;
                    } else {
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (elementsAmount == 0) {
            return null;
        }
        arrayIndex = (key == null ? 0 : Math.abs(key.hashCode() % valuesArray.length));
        Node<K,V> currentNode = valuesArray[arrayIndex];
        if (currentNode == null) {
            return null;
        }
        while (true) {
            if (currentNode.nodeKey != null && currentNode.nodeKey.equals(key)
                    || currentNode.nodeKey == null && key == null) {
                return currentNode.nodeValue;
            } else if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                return null;
            }
        }
    }

    @Override
    public int getSize() {
        return elementsAmount;
    }

    private void resize() {
        Node<K,V>[] newValuesArray = new Node[valuesArray.length * 2];
        arrayCopy(newValuesArray);
    }

    private void arrayCopy(Node<K,V>[] newValuesArray) {
        Node<K,V>[] oldArray = valuesArray;
        valuesArray = newValuesArray;
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] != null) {
                Node<K,V> currentNode = oldArray[i];
                while (true) {
                    arrayIndex = (currentNode.nodeKey == null ? 0
                            : Math.abs(currentNode.nodeKey.hashCode() % valuesArray.length));
                    if (valuesArray[arrayIndex] == null) {
                        valuesArray[arrayIndex] = new Node<>(currentNode.nodeKey,
                                currentNode.nodeValue, null);
                    } else {
                        Node<K,V> newArrayNode = valuesArray[arrayIndex];
                        while (true) {
                            if (newArrayNode.next == null) {
                                newArrayNode.next = new Node<>(currentNode.nodeKey,
                                        currentNode.nodeValue, null);
                                break;
                            } else {
                                newArrayNode = newArrayNode.next;
                            }
                        }
                    }
                    if (currentNode.next == null) {
                        break;
                    } else {
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private static class Node<K,V> {
        private V nodeValue;
        private final K nodeKey;
        private Node<K,V> next;

        Node(K nodeKey, V nodeValue, Node<K,V> next) {
            this.nodeKey = nodeKey;
            this.nodeValue = nodeValue;
            this.next = next;
        }
    }
}

