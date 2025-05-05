package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int currentCapacity;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        currentCapacity = INITIAL_CAPACITY;
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);
        Node<K,V> newNode = new Node<>(key, value, null);
        table[index] = putANewNode(index, table, newNode);
    }

    @Override
    public V getValue(K key) {
        int indexOfSearchedElement = calculateIndex(key);
        if (table[indexOfSearchedElement] == null) {
            return null;
        }
        Node<K,V> currentNode = table[indexOfSearchedElement];
        Node<K,V> foundElement = null;
        while (currentNode != null) {
            if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                foundElement = currentNode;
                break;
            }
            currentNode = currentNode.next;
        }
        return foundElement != null ? foundElement.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateIndex(K key) {
        int index = (key != null) ? (Math.floorMod(key.hashCode(), currentCapacity)) : 0;
        return index;
    }

    private Node<K,V> putANewNode(int index, Node<K,V>[] tableToBeAddedTo, Node<K,V> nodeToPut) {
        boolean isSizeIncrease = true;
        if (tableToBeAddedTo[index] != null) {
            isSizeIncrease =
             putNewNodeToCurrentCapacityTableNode(tableToBeAddedTo,index,nodeToPut,isSizeIncrease);
        } else {
            tableToBeAddedTo[index] = new Node<>(nodeToPut.key, nodeToPut.value, null);
        }
        if (isSizeIncrease) {
            size++;
            if (size == threshold) {
                increaseCurrentCapacityAndThreshold();
                createAndMoveToNewTableIfThresholdExceeded();
            }
        }
        return tableToBeAddedTo[index];
    }

    private void createAndMoveToNewTableIfThresholdExceeded() {
        Node<K,V>[] newTable = new Node[currentCapacity];
        table = tableTransfer(newTable,currentCapacity);
    }

    private Node<K,V>[] tableTransfer(Node<K,V>[] newTable, int currentCapacity) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                continue;
            }
            Node<K,V> transferredNode = table[i];
            int newTableIndex;
            while (transferredNode != null) {
                newTableIndex = calculateIndex(transferredNode.key);
                // detached function
                if (newTable[newTableIndex] != null) {
                    putTransferredNodeToExtendedTableNode(newTableIndex, newTable, transferredNode);
                } else {
                    newTable[newTableIndex] = new Node<>(transferredNode.key,
                            transferredNode.value, null);
                }
                transferredNode = transferredNode.next;
            }
        }
        return newTable;
    }

    private void putTransferredNodeToExtendedTableNode(
            int newTableIndex, Node<K,V>[] newTable, Node<K,V> transferredNode) {
        Node<K,V> currentNodeOfTableToBeAddedTo = newTable[newTableIndex];
        boolean areNodeKeysEqual = false;
        while (currentNodeOfTableToBeAddedTo.next != null) {
            if (transferredNode.key == currentNodeOfTableToBeAddedTo.key
                    || transferredNode.key != null
                    && transferredNode.key.equals(currentNodeOfTableToBeAddedTo.key)) {
                currentNodeOfTableToBeAddedTo.value = transferredNode.value;
                areNodeKeysEqual = true;
                break;
            } else {
                currentNodeOfTableToBeAddedTo = currentNodeOfTableToBeAddedTo.next;
            }
        }
        if (!areNodeKeysEqual) {
            currentNodeOfTableToBeAddedTo.next =
                    new Node<K, V>(transferredNode.key, transferredNode.value, null);
        }
    }

    private boolean putNewNodeToCurrentCapacityTableNode(
            Node<K,V>[] tableToBeAddedTo, int index, Node<K,V> nodeToPut,
            boolean isSizeIncrease) {
        Node<K,V> currentNodeOfTableToBeAddedTo = tableToBeAddedTo[index];
        while (currentNodeOfTableToBeAddedTo != null) {
            if (nodeToPut.key == currentNodeOfTableToBeAddedTo.key
                    || nodeToPut.key != null
                    && nodeToPut.key.equals(currentNodeOfTableToBeAddedTo.key)) {
                currentNodeOfTableToBeAddedTo.value = nodeToPut.value;
                isSizeIncrease = false;
                break;
            }
            if (currentNodeOfTableToBeAddedTo.next == null) {
                break;
            }
            currentNodeOfTableToBeAddedTo = currentNodeOfTableToBeAddedTo.next;
        }
        if (isSizeIncrease) {
            currentNodeOfTableToBeAddedTo.next =
                    new Node<K, V>(nodeToPut.key, nodeToPut.value, null);
        }
        return isSizeIncrease;
    }

    private void increaseCurrentCapacityAndThreshold() {
        currentCapacity = currentCapacity << 1;
        threshold = threshold << 1;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
